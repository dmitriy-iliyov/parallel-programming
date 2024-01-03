import org.example.Bank;
import org.example.TransferThread;
import org.junit.Test;


public class BankTest {
    private Bank bank = new Bank();
    @Test
    public void testTransfer(){
        int threadCount = 1000;
        int accountCount = 100;
        for(int i = 0; i < accountCount; i++){
            bank.addAccount();
        }
        System.out.println("Start sum = " + bank.calculateTotalAmount());
        for (int i = 0; i < threadCount; i++){
            TransferThread transferThread = new TransferThread(bank, accountCount);
            transferThread.start();
            try {
                transferThread.join();
            }catch (InterruptedException e){}
        }
        System.out.println("End sum  =  " + bank.calculateTotalAmount());
        System.out.println(bank.count);
        System.out.println(bank.failsCount);
    }
}
