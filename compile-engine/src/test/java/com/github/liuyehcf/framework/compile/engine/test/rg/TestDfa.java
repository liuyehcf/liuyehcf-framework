package com.github.liuyehcf.framework.compile.engine.test.rg;

import com.github.liuyehcf.framework.compile.engine.rg.Matcher;
import com.github.liuyehcf.framework.compile.engine.rg.RGBuilder;
import com.github.liuyehcf.framework.compile.engine.rg.utils.AbstractTestCaseBuilder;
import org.junit.Test;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.regex.Pattern;

import static org.junit.Assert.assertTrue;

/**
 * Created by Liuye on 2017/10/24.
 */
public class TestDfa {

    private void testRegexGroup(String[] regexGroup,
                                boolean testAllPossibleCases,
                                int randomTimes,
                                boolean testGroup) {
        for (String regex : regexGroup) {
            testEachRegex(regex,
                    testAllPossibleCases,
                    randomTimes,
                    testGroup);
        }
    }

    private void testEachRegex(String regex,
                               boolean testAllPossibleCases,
                               int randomTimes,
                               boolean testGroup) {
        com.github.liuyehcf.framework.compile.engine.rg.Pattern compiler = RGBuilder.compile(regex).buildDfa();

        Pattern pattern = Pattern.compile(regex);
        List<String> unPassedCases = null;

        try {
            if (testAllPossibleCases) {
                Set<String> matchedCases = AbstractTestCaseBuilder.createAllOptionalTestCasesWithRegex(regex);
                unPassedCases = testDfaWithMatchedCases(pattern, compiler, matchedCases, testGroup);
            }

            Set<String> matchedCases = AbstractTestCaseBuilder.createRandomTestCasesWithRegex(regex, randomTimes);
            unPassedCases = testDfaWithMatchedCases(pattern, compiler, matchedCases, testGroup);
            assertTrue(unPassedCases.isEmpty());
        } catch (AssertionError e) {
            System.err.println("Regex: [" + regex + "], unPassedCases: " + unPassedCases);
            throw e;
        }
    }

    private List<String> testDfaWithMatchedCases(Pattern pattern, com.github.liuyehcf.framework.compile.engine.rg.Pattern compiler,
                                                 Set<String> matchedCases,
                                                 boolean testGroup) {
        List<String> unPassedCases = new ArrayList<>();
        for (String matchedCase : matchedCases) {

            java.util.regex.Matcher jdkMatcher = pattern.matcher(matchedCase);
            Matcher dfaMatcher = compiler.matcher(matchedCase);

            assertTrue(jdkMatcher.matches());

            if (!dfaMatcher.matches()) {
                unPassedCases.add(matchedCase);
                continue;
            }

            if (testGroup) {
                for (int group = 0; group < jdkMatcher.groupCount(); group++) {
                    String jdkGroup = jdkMatcher.group(group);
                    String dfaGroup = dfaMatcher.group(group);

                    if (jdkGroup == null) {
                        if (dfaGroup != null) {
                            unPassedCases.add(matchedCase);
                        }
                    } else {
                        if (!jdkGroup.equals(dfaGroup)) {
                            unPassedCases.add(matchedCase);
                        }
                    }
                }
            }
        }

        return unPassedCases;
    }

    @Test
    public void testGroup1() {
        testRegexGroup(TestRegex.REGEX_GROUP_1,
                true,
                1000,
                false);
    }

    @Test
    public void testGroup2() {
        testRegexGroup(TestRegex.REGEX_GROUP_2,
                true,
                1000,
                false);
    }

    @Test
    public void testGroup3() {
        testRegexGroup(TestRegex.REGEX_GROUP_3,
                true,
                1000,
                false);
    }

    @Test
    public void testGroup4() {
        testRegexGroup(TestRegex.REGEX_GROUP_4,
                true,
                1000,
                false);
    }

    @Test
    public void testGroup5() {
        testRegexGroup(TestRegex.REGEX_GROUP_5,
                true,
                1000,
                false);
    }

    @Test
    public void testGroup6() {
        testRegexGroup(TestRegex.REGEX_GROUP_6,
                true,
                1000,
                false);
    }

    @Test
    public void testGroup7() {
        testRegexGroup(TestRegex.REGEX_GROUP_7,
                false,
                1000,
                false);
    }

    @Test
    public void testGroup8() {
        testRegexGroup(TestRegex.REGEX_GROUP_8,
                false,
                1000,
                false);
    }

    @Test
    public void testGroup9() {
        testRegexGroup(TestRegex.REGEX_GROUP_9,
                false,
                1000,
                false);
    }

    @Test
    public void testGroupSpecial() {
        testRegexGroup(TestRegex.REGEX_GROUP_SPECIAL,
                false,
                1000,
                false);
    }

}