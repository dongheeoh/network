package thread;

public class MultiThreadEx03 {

	public static void main(String[] args) {
		Thread thread1=new AlphabeticThread();
		Thread thread2=new DigitThread();
		Runnable runnable=new UppercaseAlpabeticRunableimpl();
		//Thread thread3=new Thread(new UppercaseAlpabeticRunableimpl()).start();
		
		thread1.start();
		thread2.start();
		//thread3.start();
		new Thread(runnable).start();
	}

}
