package com.clemble.social.spring.utils.synonyms;

import org.junit.runner.RunWith;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import com.clemble.social.utils.synonyms.spring.configuration.SynonymServiceConfiguration;


@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes = SynonymServiceConfiguration.class)
@ActiveProfiles(value = "synonymTest")
abstract public class AbstractSynonymsTest {

}
