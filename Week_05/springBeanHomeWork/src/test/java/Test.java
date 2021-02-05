import io.kimmking.spring02.School;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;


@SpringBootTest
public class Test {

    @Autowired
    private School school;

    @org.junit.jupiter.api.Test
    public void testStarter(){

    }
}
