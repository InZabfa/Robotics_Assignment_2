//calculate the price for this agent to complete the task sent by the doctor
price(_Service,X) :- .random(R) & X = (10*R)+100.

//starts speaking with the doctor agent and runs plays belief
plays(initiator,doctor).


//Introduces itself to the doctor agent so that it could send the instructions
+plays(initiator,In)
   :  .my_name(Me)
   <- .send(In,tell,introduction(participant,Me)).
   
// answer to Call For Proposal from Doctor agent
// [source(A)] is a doctor
// plays sends the initial communication name and the price
// remember the proposal in the mental notes
// tell doctor that this agent will take the task proposed

@c1 +cfp(CNPId,Task,C,NC)[source(A)]
   :  plays(initiator,A) & price(Task,Offer)
   <- +proposal(CNPId,Task,C,NC,Offer);		// remember my proposal
      .send(A,tell,propose(CNPId,Offer)).

// Handling an Accept message
// 
@r1 +accept_proposal(CNPId)[source(A)]
		: proposal(CNPId,Task,C,NC,Offer)
		<- !getScenario(A);
		    +startRescueMission(A,C,NC).
			
+!getScenario(D) <- .send(D,askAll,location(_,_,_)).
 
// Handling a Reject message
@r2 +reject_proposal(CNPId)
		<- .print("I lost CNP ",CNPId, ".");
		msg("reject_proposal", "I lost CNP ",CNPId, ".");
		// clear memory
		-proposal(CNPId,_,_).

//Adds victims
+location(victim,X,Y)[source(D)]: plays(initiator,D)
    <- addVictim(X,Y).

//Adds obstacles
+location(obstacle,X,Y)[source(D)]: plays(initiator,D) 
	<- addObstacle(X,Y).

//Adds hospital
+location(hospital,X,Y)[source(D)]: plays(initiator,D) 
	<- addHospital(X,Y).
    
//receives information from the Doctor has values mental note (considered as new intention)
+startRescueMission(D,C,NC) : location(hospital,X,Y) & 
							  location(victim,_,_) &
							  location(obstacle,_,_) 
    <- +criticalCount(C); 
       +nonCriticalCount(NC);
	   .print("The amount of critical: ", C , ". Amount of non-critical: ", NC);
	   msg("startRescueMission", "The amount of critical: ", C , ". Amount of non-critical: ", NC);
       !start.

+startRescueMission(D,C,NC)
    <- .wait(5000);
       -+startRescueMission(D,C,NC).

//Loop until connected
+!start : not connected
	<- update;
	.print("Not connected - trying again.");
	msg("start", "Not connected - trying again.");
	.wait(5000);
	!start.
	
//When connected, start reachedGoal method
+!start : connected
	<- update;
	.print("Starting rescue missions.");
	msg("start", "Starting rescue missions.");
	!getNextClosestVictim.
	

+getNextClosestVictim(P,X,Y)
    <- update;
    .print("The proximity to (",X,",",Y,") is ",P);
    msg("getNextClosestVictim", "The proximity to (",X,",",Y,") is ",P);
    .wait(500).

+!getNextClosestVictim : location(victim,X,Y) & not costOfPathToVictim(_,X,Y) & not location(nonCritical,X,Y)
    <- update;
    .print("Calculating path of ", X, "," , Y);
    msg("getNextClosestVictim", "Calculating path of ", X, "," , Y);
    calculateCostOfPath(X,Y);
    .wait(500);
	!getNextClosestVictim.
	
+!getNextClosestVictim : criticalCount(0) & nonCriticalCount(NC) & location(nonCritical,X,Y) & location(hospital,HX,HY)
	<- update;
	.wait(5000);
	.print("Going to Victim...");
	msg("getNextClosestVictim", "Going to Victim...");
	moveToLocation(X,Y);
  	.wait(1000);
  	!awaitRobot.

	
// If we have more victims and list of nonCritical is empty.
+!getNextClosestVictim : location(victim,_,_)  & not location(nonCritical,_,_)
	<- 	update;
	.findall(moveTO(P,X,Y),costOfPathToVictim(P,X,Y),L);
	.min(L,moveTO(P,X,Y));
	.print("Closest: ", moveTO(P,X,Y));
	msg("getNextClosestVictim","Closest: ", moveTO(P,X,Y));
	.print("Path cost of (", X, ",", Y, ") is ", P);
	msg("getNextClosestVictim","Path cost of (", X, ",", Y, ") is ", P);
	moveToLocation(X,Y);
	.print("Requesting move to: ", X , "," , Y);
	msg("getNextClosestVictim","Requesting move to: ", X , "," , Y);
	.wait(2000);
	!awaitRobot.
  		
 //If we have non critical
+!getNextClosestVictim : location(nonCritical,_,_)
	<- 	update;
	.findall(moveTO(P,X,Y),costOfPathToVictim(P,X,Y),L);
	.min(L,moveTO(P,X,Y));
	.print("Closest: ", moveTO(P,X,Y));
	msg("getNextClosestVictim", "Closest: ", moveTO(P,X,Y));
	.print("Path cost of (", X, ",", Y, ") is ", P);
	msg("getNextClosestVictim", "Path cost of (", X, ",", Y, ") is ", P);
	moveToLocation(X,Y);
	.print("Requesting move to: ", X , "," , Y);
	msg("getNextClosestVictim", "Requesting move to: ", X , "," , Y);
	.wait(1000);
	!awaitRobot.
  		
+!getNextClosestVictim : not atHospital & criticalCount(0) & nonCriticalCount(0) & location(hospital,HX,HY)
	<- 	moveToLocation(HX,HY);
	update;
	clearPercepts;
	!endFunctionality.
  		
+!awaitRobot : inTransit
	<- .wait(1000);
	update;
	!awaitRobot.

//If at goal but not at hospital
+!awaitRobot : not atHospital & atGoal & not nonCriticalCount(0)
	<- update;
	.wait(2000);
	.print("Checking colour...");
	msg("awaitRobot","Checking colour...");
	!checkColour.

//If at goal but at hospital
+!awaitRobot : atGoal & atHospital & not nonCriticalCount(0)
	<- update;
	clearPercepts;
	.wait(1000);
	.print("Getting next victim...");
	msg("awaitRobot", "Getting next victim...");
	!getNextClosestVictim.

+!awaitRobot : atGoal & atHospital & inTransit & criticalCount(0) & nonCriticalCount(0)
	<- !endFunctionality.
	
	
+!awaitRobot: criticalCount(0) & nonCriticalCount(0) & atHospital
	<- clearPercepts;
	.wait(500);
	!endFunctionality.
	
+!checkColour : not hasColour(X,Y,C) & not skipColourCheck & not atHospital
	<- update;
	.wait(2000);
	.print("Requesting colour from environment...");
	msg("checkColour","Requesting colour from environment...");
	requestColour;
	.wait(2000);
	!checkColour.
	
+!checkColour : hasColour(X,Y,C) & not location(nonCritical,X,Y)
	<- update;
	.wait(2000);
	.print("Getting colour from doctor - given by environment...");
	msg("checkColour","Getting colour from doctor - given by environment...");
	.print("Colour at (", X , "," , Y , ") is ", C);
	msg("checkColour","Colour at (", X , "," , Y , ") is ", C);
	gotColour(X,Y,C);
	.send(doctor, tell, requestVictimStatus(X,Y,C)).

//If we have nonCritical XY
+!checkColour : nonCriticalCount(NC) & location(victim,X,Y)[source(D)] & location(hospital,HX,HY) & hasColour(X,Y,C) & location(nonCritical,X,Y)
	<- update;
	.wait(2000);
	.print("Going hospital...");
	msg("checkColour","Going hospital...");
	moveToLocation(HX,HY);
	savednoncritical(X,Y);
	-location(victim,X,Y)[source(D)];
    -+nonCriticalCount(NC-1);
    -location(nonCritical,X,Y);
	!awaitRobot.

+!checkColour : locationWithNoColour(X,Y) & location(victim,X,Y)[source(D)] & skipColourCheck
	<- update;
	.wait(2000);
	saved(X,Y);
	-location(victim,X,Y)[source(D)];
	.print("Removed the current location of the victim from the list (", X, ",", Y, ")...");
	msg("checkColour","Removed the current location of the victim from the list (", X, ",", Y, ")...");
	remove(X,Y);
	clearPercepts;
	!getNextClosestVictim.
	
	
//These are generated through communication with the
// doctor agents, via the achievement task +!requestVictimStatus(D,X,Y,C)      
+critical(X,Y): location(hospital,HX,HY) & location(victim,X,Y)[source(D)] & criticalCount(C)
    <- update;
    .wait(2000);
    .print("The victim at ", X, ",", Y, " is critical");
    msg("critical{true}", "The victim at ", X, ",", Y, " is critical");
    savedcritical(X,Y);
    -location(victim,X,Y)[source(D)]; 
    .print("Removed the current victim :", X , Y);
    msg("critical{true}","Removed the current victim :", X , Y);
    moveToLocation(HX,HY);
    .print("Going to the hospital...");
    msg("critical{true}","Going to the hospital...");
    -+criticalCount(C-1);
    !awaitRobot.
    
+~critical(X,Y): location(hospital,HX,HY) & location(victim,X,Y)[source(D)]  & not location(nonCritical,X,Y)  & criticalCount(0) & nonCriticalCount(NC)
    <- update;
    .wait(2000); 
    .print("The victim at ", X, ",", Y, " is not critical, attending...");
    msg("critical{false}","The victim at ", X, ",", Y, " is not critical, attending...");
    moveToLocation(HX,HY);
    savednoncritical(X,Y);
    -location(victim,X,Y)[source(D)];
    -+nonCriticalCount(NC-1);
    .print("Going to the hospital...");
    msg("critical{false}","Going to hospital...");
    !awaitRobot.
        
+~critical(X,Y): location(victim,X,Y)[source(D)] & not criticalCount(0) & not location(nonCritical,X,Y) 
    <- update;
    .wait(2000); 
    .print("The victim at ", X, ",", Y, " is not critical, ignoring...");
    msg("critical{false}","The victim at ", X, ",", Y, " is not critical, ignoring...");
   	+location(nonCritical,X,Y);
   	clearPercepts;
   	!getNextClosestVictim.
   	
+!endFunctionality
	<- .print("All patients have been seen. Terminating functionality.");
	msg("endFunctionality","All patients have been seen. Terminating functionality.");
	finish.