package test.com.hisense.springboot.service;

import com.hisense.springboot.SpringbootTestApplication;
import com.hisense.springboot.service.CacheVehicleInfoService;
import org.junit.Test;
import org.junit.Before;
import org.junit.After;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

/**
 * CacheVehicleInfoServiceImpl Tester.
 *
 * @author <Authors name>
 * @version 1.0
 * @since <pre></pre>11, 2018</pre>
 */
@SpringBootTest(classes = SpringbootTestApplication.class)
@RunWith(SpringJUnit4ClassRunner.class)
public class CacheVehicleInfoServiceImplTest {

    @Autowired
    private CacheVehicleInfoService cacheVehicleInfoService;

    @Before
    public void before() throws Exception {
    }

    @After
    public void after() throws Exception {
    }

    /**
     * Method: save(VehicleInfo vehicleInfo)
     */
    @Test
    public void testSave() throws Exception {

//                StringBuffer buffer = new StringBuffer();
                //key plate+colortype
//                buffer.append(item[5] + ",");
//                buffer.append(item[8]);
//                System.out.println(buffer);
//                buffer.setLength(0);
                //value deviceId+time
//                buffer.append(item[1]);
//                buffer.append(item[21]);
//                System.out.println(buffer);
//                System.out.printf("offset = %d, value = %s", record.offset(), record.value());
//                System.out.println();

    }


    /**
     * Method: findById(VehicleInfo vehicleInfo)
     */
    @Test
    public void testFindById() throws Exception {
//TODO: Test goes here... 
    }

    /**
     * Method: findAll()
     */
    @Test
    public void testFindAll() throws Exception {
//TODO: Test goes here... 
    }

    /**
     * Method: update(VehicleInfo vehicleInfo)
     */
    @Test
    public void testUpdate() throws Exception {
//TODO: Test goes here... 
    }

    /**
     * Method: delete(VehicleInfo vehicleInfo)
     */
    @Test
    public void testDelete() throws Exception {
//TODO: Test goes here... 
    }


} 
