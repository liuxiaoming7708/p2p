import com.payease.p2p.business.domain.BidRequest;
import com.payease.p2p.business.service.IBidRequestService;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

/**
 * Created by liuxiaoming on 2017/7/28.
 */
@RunWith(SpringJUnit4ClassRunner.class)		//表示继承了SpringJUnit4ClassRunner类
@ContextConfiguration(locations = {"classpath:applicationContext_website.xml"})
public class TestWeb {

    @Autowired
    private IBidRequestService bidRequestService;


    @Test
    public void test1(){
        BidRequest bidRequest = this.bidRequestService.get(2L);
        System.out.println(bidRequest.getBids());
    }
}
