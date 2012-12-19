package com.socialone.utils.soundmatch;


import org.junit.runner.RunWith;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import com.socialone.utils.soundmatch.spring.configuration.SoundmatchServiceConfiguration;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes = SoundmatchServiceConfiguration.class)
@ActiveProfiles(value = "soundmatchTest")
abstract public class AbstractBusinessTierTest {

}
