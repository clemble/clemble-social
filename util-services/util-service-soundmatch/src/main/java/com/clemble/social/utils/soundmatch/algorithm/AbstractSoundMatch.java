package com.clemble.social.utils.soundmatch.algorithm;

import static com.google.common.base.Preconditions.checkNotNull;

import java.util.Comparator;

import org.apache.commons.codec.EncoderException;
import org.apache.commons.codec.StringEncoder;
import org.springframework.beans.factory.annotation.Autowired;

import com.clemble.social.service.match.MatchScore;
import com.clemble.social.utils.soundmatch.SoundMatch;
import com.clemble.social.utils.soundmatch.SoundMatchAlgorithm;
import com.clemble.social.utils.soundmatch.SoundMatchDataRepository;

abstract public class AbstractSoundMatch implements SoundMatch {

    @Autowired(required = false)
    private SoundMatchDataRepository matchDataRepository;
    
    final private StringEncoder stringEncoder;
    
    final private SoundMatchAlgorithm soundMatchAlgorithm;
    
    final private Comparator<String> presentationComparator;
    
    protected AbstractSoundMatch(StringEncoder stringEncoder, Comparator<String> comparator, SoundMatchAlgorithm soundMatchAlgorithm) {
        this.stringEncoder = checkNotNull(stringEncoder);
        this.presentationComparator = checkNotNull(comparator);
        this.soundMatchAlgorithm = checkNotNull(soundMatchAlgorithm);
    }

    @Override
    public MatchScore match(String source, String compared) {
        // Step 1. Sanity check
        if (source == null || compared == null)
            return MatchScore.NO_MATCH;
        if (source.equals(compared))
            return MatchScore.FULL_MATCH;
        // Step 2. Check values
        String sourcePresentaion = null;
        String comparedPresentation = null;
        try {
            sourcePresentaion = getPresentation(source);
            comparedPresentation = getPresentation(compared);
        } catch (EncoderException e) {
            return MatchScore.INCONCLUSIVE;
        } catch (IllegalArgumentException illegalAccessException) {
            return MatchScore.INCONCLUSIVE;
        } catch(RuntimeException runtimeException) {
            return MatchScore.INCONCLUSIVE;
        }
        if(sourcePresentaion == null || comparedPresentation == null || sourcePresentaion.length() == 0 || comparedPresentation.length() == 0)
            return MatchScore.INCONCLUSIVE;
        // Step 3. Checking encoded values
        int comparisonScore = presentationComparator.compare(sourcePresentaion, comparedPresentation);
        return MatchScore.valueOf(comparisonScore);
    }

    private String getPresentation(String word) throws EncoderException {
        String presentation = null;
        if(matchDataRepository != null) {
            presentation = matchDataRepository.get(word, getSoundMatchAlgorithm());
            if (presentation == null) {
                presentation = stringEncoder.encode(word);
                matchDataRepository.put(word, presentation, getSoundMatchAlgorithm());
            }
        } else {
            presentation = stringEncoder.encode(word);
        }
        return presentation;
    }

    @Override
    public SoundMatchAlgorithm getSoundMatchAlgorithm() {
        return soundMatchAlgorithm;
    }

    public StringEncoder getStringEncoder() {
        return stringEncoder;
    }

}
