package cz.muni.fi.pv260.productfilter;


import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import static org.mockito.Matchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class AtLeastNOfFilterTest {

    @Test(expected = IllegalArgumentException.class)
    public void constructorThrowsIllegalArgumentException_nLowerThan0() {
        AtLeastNOfFilter<Object> filter = new AtLeastNOfFilter<>(-1);
    }

    @Test(expected = IllegalArgumentException.class)
    public void constructorThrowsIllegalArgumentException_nEquals0() {
        AtLeastNOfFilter<Object> filter = new AtLeastNOfFilter<>(0);
    }


    @Test(expected = FilterNeverSucceeds.class)
    public void constructorThrowsFilterNeverSucceeds_nMoreThanFilters() {
        AtLeastNOfFilter<Object> filter = new AtLeastNOfFilter<>(1);
    }

    @Test
    public void constructorSucceeds() {
        Filter passesFilter = passesFilter();
        AtLeastNOfFilter<Object> filter = new AtLeastNOfFilter<>(1, passesFilter);
    }

    @Test
    public void filterPasses_IfNFiltersPass() {
        Filter passesFilter = passesFilter();
        Filter failsFilter = failsFilter();
        AtLeastNOfFilter<Object> filter = new AtLeastNOfFilter<>(1, passesFilter, failsFilter);

        Assert.assertTrue(filter.passes(any()));
    }

    @Test
    public void filterFails_IfNminus1FiltersPass() {
        Filter passesFilter = passesFilter();
        Filter failsFilter = failsFilter();
        AtLeastNOfFilter<Object> filter = new AtLeastNOfFilter<>(2, passesFilter, failsFilter);

        Assert.assertFalse(filter.passes(any()));
    }

    private Filter<Object> passesFilter() {
        Filter<Object> filter = mock(Filter.class);
        when(filter.passes(any())).thenReturn(true);
        return filter;
    }

    private Filter<Object> failsFilter() {
        Filter<Object> filter = mock(Filter.class);
        when(filter.passes(any())).thenReturn(false);
        return filter;
    }
}
