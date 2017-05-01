package net.lovexq.seckill.kernel.repository;

import net.lovexq.seckill.kernel.model.SysUserModel;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.time.LocalDateTime;

/**
 * Created by 品冬 on 2017-4-30.
 */
@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class SysUserRepositoryTest {

    @Autowired
    private SysUserRepository sysUserRepository;

    @Test
    public void testBatchAddUser() {
        SysUserModel newUser;
        SysUserModel oldUser = sysUserRepository.findOne(343142L);
        Long index = 343142L;
        while (true) {
            index++;
            newUser = new SysUserModel();
            String indexStr = index.toString();

            newUser.setId(index);
            newUser.setPassword("57dae71be50ea0849e2f1b6abd243613");
            newUser.setAccount(oldUser.getAccount().replace("4", indexStr));
            newUser.setName(oldUser.getName().replace("4", indexStr));
            newUser.setNickname(oldUser.getNickname().replace("4", indexStr));
            newUser.setAccount(oldUser.getAccount().replace("4", indexStr));
            newUser.setMobile(String.valueOf(Long.parseLong(oldUser.getMobile()) + index));
            newUser.setEmail(oldUser.getEmail().replace("4", indexStr));
            newUser.setCreateTime(LocalDateTime.now());
            newUser.setUpdateTime(LocalDateTime.now());
            newUser.setLocked(false);
            newUser.setState(true);
            sysUserRepository.saveAndFlush(newUser);
        }
    }
}