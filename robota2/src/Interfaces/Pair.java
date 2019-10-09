package Interfaces;


public class Pair{
    private String key;
    private String value;

    /**
     * Used to intialise a Pair class.
     * @param key
     * @param value
     */
    public Pair(String key, String value){
        this.key = key;
        this.value = value;
    }

    /**
     * Used to retrieve the key of this pair.
     * @return Key
     */
    public String Key(){
        return this.key;
    }

    /**
     * Used to retrieve the value of this pair
     * @return Value
     */
    public String Value(){
        return this.value;
    }
}