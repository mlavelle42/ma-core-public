/**
 * @copyright 2017 {@link http://infiniteautomation.com|Infinite Automation Systems, Inc.} All rights reserved.
 * @author Terry Packer
 */
package com.serotonin.m2m2.dao;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import com.serotonin.m2m2.Common;
import com.serotonin.m2m2.MangoTestBase;

/**
 *
 * @author Terry Packer
 */
public class NumericPointValueDaoTest extends MangoTestBase{
    
    private NumericPointValueDaoTestHelper helper;

    @Before
    public void before() {
        super.before();
        this.helper = new NumericPointValueDaoTestHelper(Common.databaseProxy.newPointValueDao());
        this.helper.before();
    }
    @After
    public void after() {
        this.helper.after();
    }
    

    //Latest Queries
    @Test
    public void testLatestExceptionInCallback() {
        this.helper.testLatestExceptionInCallback();
    }
    @Test
    public void testLatestNoDataInBothSeries() {
        this.helper.testLatestNoDataInBothSeries();
    }
    @Test
    public void testLatestNoDataInOneSeries() {
        this.helper.testLatestNoDataInOneSeries();
    }
    @Test
    public void testLatestMultiplePointValuesNoLimit() {
        this.helper.testLatestMultiplePointValuesNoLimit();
    }
    @Test
    public void testLatestMultiplePointValuesNoLimitOffsetSeries() {
        this.helper.testLatestMultiplePointValuesNoLimitOffsetSeries();
    }
    @Test
    public void testLatestMultiplePointValuesOrderByIdNoLimit() {
        this.helper.testLatestMultiplePointValuesOrderByIdNoLimit();
    }
    @Test
    public void testLatestMultiplePointValuesOrderByIdNoLimitOffsetSeries() {
        this.helper.testLatestMultiplePointValuesOrderByIdNoLimitOffsetSeries();
    }
    @Test
    public void testLatestMultiplePointValuesLimit() {
        this.helper.testLatestMultiplePointValuesLimit();
    }
    @Test
    public void testLatestMultiplePointValuesLimitOffsetSeries() {
        this.helper.testLatestMultiplePointValuesLimitOffsetSeries();
    }
    @Test
    public void testLatestMultiplePointValuesOrderByIdLimit() {
        this.helper.testLatestMultiplePointValuesOrderByIdLimit();
    }
    @Test
    public void testLatestMultiplePointValuesOrderByIdLimitOffsetSeries() {
        this.helper.testLatestMultiplePointValuesOrderByIdLimitOffsetSeries();
    }

    //Values Between
    @Test
    public void testBetweenExceptionInCallback() {
        this.helper.testBetweenExceptionInCallback();
    }
    @Test
    public void testBetweenNoDataInBothSeries() {
        this.helper.testBetweenNoDataInBothSeries();
    }
    @Test
    public void testBetweenNoDataInOneSeries() {
        this.helper.testBetweenNoDataInOneSeries();
    }
    @Test
    public void testRangeMultiplePointValuesNoLimit() {
        this.helper.testRangeMultiplePointValuesNoLimit();
    }
    @Test
    public void testRangeMultiplePointValuesNoLimitOffsetSeries() {
        this.helper.testRangeMultiplePointValuesNoLimitOffsetSeries();
    }
    @Test
    public void testRangeMultiplePointValuesOrderByIdNoLimit() {
        this.helper.testRangeMultiplePointValuesOrderByIdNoLimit();
    }
    @Test
    public void testRangeMultiplePointValuesOrderByIdNoLimitOffsetSeries() {
        this.helper.testRangeMultiplePointValuesOrderByIdNoLimitOffsetSeries();
    }
    @Test
    public void testRangeMultiplePointValuesLimit() {
        this.helper.testRangeMultiplePointValuesLimit();
    }
    @Test
    public void testRangeMultiplePointValuesLimitOffsetSeries() {
        this.helper.testRangeMultiplePointValuesLimitOffsetSeries();
    }
    @Test
    public void testRangeMultiplePointValuesOrderByIdLimit() {
        this.helper.testRangeMultiplePointValuesOrderByIdLimit();
    }
    @Test
    public void testRangeMultiplePointValuesOrderByIdLimitOffsetSeries() {
        this.helper.testRangeMultiplePointValuesOrderByIdLimitOffsetSeries();
    }
 
    //Bookend
    @Test
    public void testBookendExceptionInFirstValueCallback() {
        this.helper.testBookendExceptionInFirstValueCallback();
    }
    @Test
    public void testBookendExceptionInRowCallback() {
        this.helper.testBookendExceptionInRowCallback();
    }
    @Test
    public void testBookendExceptionInLastValueCallback() {
        this.helper.testBookendExceptionInLastValueCallback();
    }
    @Test
    public void testBookendNoDataInOneSeries() {
        this.helper.testBookendNoDataInOneSeries();
    }
    @Test
    public void testBookendNoDataInBothSeries() {
        this.helper.testBookendNoDataInBothSeries();
    }
    @Test
    public void testBookendMultiplePointValuesNoLimit() {
        this.helper.testBookendMultiplePointValuesNoLimit();
    }
    @Test
    public void testBookendMultiplePointValuesNoLimitOffsetSeries() {
        this.helper.testBookendMultiplePointValuesNoLimitOffsetSeries();
    }
    @Test
    public void testBookendMultiplePointValuesOrderByIdNoLimit() {
        this.helper.testBookendMultiplePointValuesOrderByIdNoLimit();
    }
    @Test
    public void testBookendMultiplePointValuesOrderByIdNoLimitOffsetSeries() {
        this.helper.testBookendMultiplePointValuesOrderByIdNoLimitOffsetSeries();
    }
    @Test
    public void testBookendMultiplePointValuesLimit() {
        this.helper.testBookendMultiplePointValuesLimit();
    }
    @Test
    public void testBookendMultiplePointValuesLimitOffsetSeries() {
        this.helper.testBookendMultiplePointValuesLimitOffsetSeries();
    }
    @Test
    public void testBookendMultiplePointValuesOrderByIdLimit() {
        this.helper.testBookendMultiplePointValuesOrderByIdLimit();
    }
    @Test
    public void testBookendMultiplePointValuesOrderByIdLimitOffsetSeries() {
        this.helper.testBookendMultiplePointValuesOrderByIdLimitOffsetSeries();
    }
    
}
