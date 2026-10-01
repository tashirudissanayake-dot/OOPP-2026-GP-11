public interface Playable {
    void play();
    default void quit(){
        System.out.println("Sorry quiting is not allowed");
    }
}
