package net.lovexq.seckill.kernel.repository;

import net.lovexq.background.system.model.SystemUserModel;
import net.lovexq.background.system.repository.SysUserRepository;
import net.lovexq.seckill.common.utils.TimeUtil;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

/**
 * Created by LuPindong on 2017-4-30.
 */
@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class SysUserRepositoryTest {

    @Autowired
    private SysUserRepository sysUserRepository;

    @Test
    public void testBatchAddUser() {
        SystemUserModel newUser;
        SystemUserModel oldUser = sysUserRepository.findOne(343142L);
        Long index = 343142L;
        while (true) {
            index++;
            newUser = new SystemUserModel();
            String indexStr = index.toString();

            newUser.setId(index);
            newUser.setPassword("57dae71be50ea0849e2f1b6abd243613");
            newUser.setAccount(oldUser.getAccount().replace("4", indexStr));
            newUser.setName(oldUser.getName().replace("4", indexStr));
            newUser.setAccount(oldUser.getAccount().replace("4", indexStr));
            newUser.setMobile(String.valueOf(Long.parseLong(oldUser.getMobile()) + index));
            newUser.setEmail(oldUser.getEmail().replace("4", indexStr));
            newUser.setCreateTime(TimeUtil.nowDateTime());
            newUser.setUpdateTime(TimeUtil.nowDateTime());
            newUser.setLocked(false);
            newUser.setState(true);
            sysUserRepository.saveAndFlush(newUser);
        }
    }
}