
public class Hello {
    public static void main(String[] args) {
        Hello h = new Hello();
        h.test();
    }

    public void test() {
        int a = 0;
        int b = 0;
        for(int i = 0; i < 6; i++) {
            b+=1;
            a = a + 1;
            a = a - 2;
            a = a * 3;
            a = a / 4;
            if(i == 5) {
                System.out.println("hello");
            }
        }
    }
}