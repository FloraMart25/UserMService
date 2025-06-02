package bt.edu.gcit.usermicroservice.exception;

public class FlowerNotFoundException extends RuntimeException{
    public FlowerNotFoundException( String message){
        super (message);
    }
    
}