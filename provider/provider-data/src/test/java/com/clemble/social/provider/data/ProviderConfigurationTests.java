package com.clemble.social.provider.data;

import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import junit.framework.Assert;

import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import com.clemble.social.data.query.PagingConfiguration;
import com.clemble.social.data.query.SortDefinition;
import com.clemble.social.provider.data.ProviderConfiguration;
import com.clemble.social.provider.data.ProviderConfiguration.ProviderConfigurationBuilder;
import com.clemble.social.provider.data.ProviderConfiguration.SimpleProviderConfigurationBuilder;
import com.clemble.social.provider.data.merge.MergeConfiguration;
import com.clemble.social.provider.data.merge.ProviderPriority;
import com.clemble.social.provider.data.merge.MergeConfiguration.ImmutableMergeConfiguration;
import com.clemble.social.provider.data.merge.MergeConfiguration.MergeConfigurationBuilder;
import com.clemble.social.provider.data.merge.ProviderPriority.ImmutableProviderPriority;
import com.clemble.social.provider.service.ProviderConfigurationRepository;
import com.clemble.test.random.ObjectGenerator;
import com.clemble.test.runners.RunInParallel;
import com.clemble.test.runners.RunTimes;
import com.clemble.test.runners.SingleRun;
import com.google.common.collect.ImmutableList;

@RunTimes(500)
@RunInParallel()
@Transactional(propagation = Propagation.REQUIRED)
public class ProviderConfigurationTests extends AbstractProviderDataTierTest {

    @Autowired
    private ProviderConfigurationRepository configurationsRepository;
    
    private ProviderConfiguration generateProviderConfigurations(String providerName) throws InstantiationException, IllegalAccessException, IllegalArgumentException, InvocationTargetException{
        ProviderConfigurationBuilder originalConfiguration = ObjectGenerator.generate(SimpleProviderConfigurationBuilder.class);
        
        originalConfiguration.setProvider(providerName);
        // Step 2. Generate provider priorities
        ProviderPriority providerPriority = new ImmutableProviderPriority(providerName, 3);
        Collection<ProviderPriority> providerPriorities = ImmutableList.<ProviderPriority>of(providerPriority);
        MergeConfiguration mergeConfiguration = new ImmutableMergeConfiguration(providerPriorities);
        // Step 2.1. Set merge configurations
        originalConfiguration.setMergeConfiguration(mergeConfiguration);
        
        return originalConfiguration;
    }
    
    @Test
    public void testAddProviderConfigurations() throws InstantiationException, IllegalAccessException, IllegalArgumentException, InvocationTargetException{
        // Step 1. Generating prior test data
        ProviderConfiguration originalConfiguration = ObjectGenerator.generate(ProviderConfiguration.class);
        // Step 2. Saving generated configurations
        configurationsRepository.addProviderConfiguration(originalConfiguration);
        // Step 3. Extracting saved data
        ProviderConfiguration extractedConfiguration = configurationsRepository.getProviderConfiguration(originalConfiguration.getProvider());
        // Step 4. Checking that extracted data matches saved data
        Assert.assertEquals(extractedConfiguration, originalConfiguration);
    }
    
    @Test
    public void testSimpleUpdateProviderConfigurations() throws InstantiationException, IllegalAccessException, IllegalArgumentException, InvocationTargetException{
        // Step 1. Generating prior test data
        String providerName = ObjectGenerator.generate(String.class);
        ProviderPriority newPriority = new ImmutableProviderPriority(ObjectGenerator.generate(String.class), ObjectGenerator.generate(Integer.class));
        // Step 2. Generating initial provider configurations
        ProviderConfiguration originalConfiguration = generateProviderConfigurations(providerName);
        // Step 3. Saving generated configurations
        configurationsRepository.addProviderConfiguration(originalConfiguration);
        ProviderConfiguration extractedConfiguration = configurationsRepository.getProviderConfiguration(providerName);
        Assert.assertEquals(extractedConfiguration, originalConfiguration);
        // Step 4. Adding configuration
        ProviderConfiguration modifiedConfiguration = new SimpleProviderConfigurationBuilder(originalConfiguration);
        ((MergeConfigurationBuilder) modifiedConfiguration.getMergeConfiguration()).addProviderPriority(newPriority);
        // Step 3. Saving generated configurations
        configurationsRepository.updateProviderConfiguration(modifiedConfiguration);
        extractedConfiguration = configurationsRepository.getProviderConfiguration(providerName);
        Assert.assertEquals(extractedConfiguration, modifiedConfiguration);
    }

    @Test
    public void testUpdateProviderConfigurations() throws InstantiationException, IllegalAccessException, IllegalArgumentException, InvocationTargetException{
        // Step 1. Generating prior test data
        String providerName = ObjectGenerator.generate(String.class);
        // Step 2. Generating initial provider configurations
        ProviderConfiguration originalConfiguration = generateProviderConfigurations(providerName);
        // Step 3. Saving generated configurations
        configurationsRepository.addProviderConfiguration(originalConfiguration);
        ProviderConfiguration extractedConfiguration = configurationsRepository.getProviderConfiguration(providerName);
        Assert.assertEquals(extractedConfiguration, originalConfiguration);
        // Step 4. Adding configuration
        ProviderConfiguration modifiedConfiguration = generateProviderConfigurations(providerName);
        // Step 3. Saving generated configurations
        configurationsRepository.updateProviderConfiguration(modifiedConfiguration);
        extractedConfiguration = configurationsRepository.getProviderConfiguration(providerName);
        Assert.assertEquals(extractedConfiguration, modifiedConfiguration);
    }
    
    @Test(expected = IllegalArgumentException.class)
    public void testUpdateNonExistentProviderConfigurations() throws InstantiationException, IllegalAccessException, IllegalArgumentException, InvocationTargetException{
        // Step 1. Adding configuration
        ProviderConfiguration modifiedConfiguration = generateProviderConfigurations(ObjectGenerator.generate(String.class));
        // Step 2. Saving generated configurations
        configurationsRepository.updateProviderConfiguration(modifiedConfiguration);
    }
    
    @Test
    public void testGetNonExistentProviderConfigurations() throws InstantiationException, IllegalAccessException, IllegalArgumentException, InvocationTargetException{
        Assert.assertNull(configurationsRepository.getProviderConfiguration(ObjectGenerator.generate(String.class)));
    }
    
    @Test
    @SingleRun
    public void testGetAllProviderConfigurations() throws IllegalArgumentException, InstantiationException, IllegalAccessException, InvocationTargetException{
        // Step 1. Generating random data
        List<ProviderConfiguration> providerConfigurations = new ArrayList<ProviderConfiguration>();
        for(char providerID = 'A'; providerID < 'Z'; providerID++) {
            providerConfigurations.add(generateProviderConfigurations(String.valueOf(providerID)));
        }
        // Step 2. Shuffling data, so it would be added in random Sequence
        Collections.shuffle(providerConfigurations);
        // Step 3. Adding Provider Configurations
        for(ProviderConfiguration providerConfiguration: providerConfigurations)
            configurationsRepository.addProviderConfiguration(providerConfiguration);
        // Step 4. Retrieving data from the repository with preconfigured sorting
        PagingConfiguration pagingConfiguration = PagingConfiguration.newBuilder()
                .addSortDefinition(new SortDefinition("provider", true))
                .build();
        // Step 5. Sort data
        Collections.sort(providerConfigurations, new Comparator<ProviderConfiguration>() {
            @Override
            public int compare(ProviderConfiguration firstProvider, ProviderConfiguration secondProvider) {
                return firstProvider.getProvider().compareTo(secondProvider.getProvider());
            }
        });
        // Step 6. Retrieve list and check data one by one
        List<ProviderConfiguration> extractedConfigurations = null;
        int position = 0;
        do {
            extractedConfigurations = configurationsRepository.getAllProviderConfigurations(pagingConfiguration);
            for(int i = 0; i < extractedConfigurations.size(); i++) {
                extractedConfigurations.get(0).equals(providerConfigurations.get(position++));
            }
        } while(extractedConfigurations.isEmpty());
    }
}
