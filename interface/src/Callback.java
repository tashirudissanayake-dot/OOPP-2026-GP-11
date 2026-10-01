interface Callback {
    void callback(int param);
}
abstract class CallbackAbs implements Callback {
    int a,b;
    void show(){
        System.out.println(a+""+b);
    }
}
