package cz.muni.fi.pv260.productfilter;

import org.junit.Test;
import org.mockito.Matchers;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;

import static org.mockito.Matchers.any;
import static org.mockito.Mockito.*;

public class ControllerTest {

    // region sends selected products tests

    @Test
    public void controllerSendsSelectedProductsToOutput() throws ObtainFailedException {
        Input input = mock(Input.class);
        Output output = mock(Output.class);
        Logger logger = mock(Logger.class);

        when(input.obtainProducts()).thenReturn(getProducts());

        Controller controller = new Controller(input, output, logger);

        controller.select(getAllPassesFilter());
        verify(output).postSelectedProducts(getProducts());
    }

    @Test
    public void controllerSendsSelectedProductsToOutput2() throws ObtainFailedException {
        Input input = mock(Input.class);
        Output output = mock(Output.class);
        Logger logger = mock(Logger.class);

        when(input.obtainProducts()).thenReturn(getProducts());

        Controller controller = new Controller(input, output, logger);

        controller.select(getAllFailsFilter());
        verify(output).postSelectedProducts(Collections.EMPTY_LIST);
    }

    @Test
    public void controllerSendsSelectedProductsToOutput3() throws ObtainFailedException {
        Input input = mock(Input.class);
        Output output = mock(Output.class);
        Logger logger = mock(Logger.class);

        when(input.obtainProducts()).thenReturn(getProducts());

        Controller controller = new Controller(input, output, logger);

        controller.select(getAllOnlyId2PassesFilter());
        List<Product> result = new ArrayList();
        result.add(new Product(2, "Prod2", Color.RED, BigDecimal.ONE));

        verify(output).postSelectedProducts(result);
    }

    // endregion

    // region Logger logs tests

    @Test
    public void controllerLoggerLogsResultSuccess() throws ObtainFailedException {
        Input input = mock(Input.class);
        Output output = mock(Output.class);
        Logger logger = mock(Logger.class);

        when(input.obtainProducts()).thenReturn(getProducts());

        Controller controller = new Controller(input, output, logger);

        controller.select(getAllPassesFilter());
        verify(logger).log(Controller.TAG_CONTROLLER, "Successfully selected " + 4
                + " out of " + getProducts().size() + " available products.");
    }

    @Test
    public void controllerLoggerLogsResultSuccess2() throws ObtainFailedException {
        Input input = mock(Input.class);
        Output output = mock(Output.class);
        Logger logger = mock(Logger.class);

        when(input.obtainProducts()).thenReturn(getProducts());

        Controller controller = new Controller(input, output, logger);

        controller.select(getAllFailsFilter());
        verify(logger).log(Controller.TAG_CONTROLLER, "Successfully selected " + 0
                + " out of " + getProducts().size() + " available products.");
    }

    @Test
    public void controllerLoggerLogsResultSuccess3() throws ObtainFailedException {
        Input input = mock(Input.class);
        Output output = mock(Output.class);
        Logger logger = mock(Logger.class);

        when(input.obtainProducts()).thenReturn(getProducts());

        Controller controller = new Controller(input, output, logger);

        controller.select(getAllOnlyId2PassesFilter());
        List<Product> result = new ArrayList();
        result.add(new Product(2, "Prod2", Color.RED, BigDecimal.ONE));

        verify(logger).log(Controller.TAG_CONTROLLER, "Successfully selected " + 1
                + " out of " + getProducts().size() + " available products.");
    }

    // endregion

    @Test
    public void controllerLogsException_obtainProductsThrowsException() throws ObtainFailedException {
        Input input = mock(Input.class);
        Output output = mock(Output.class);
        Logger logger = mock(Logger.class);


        when(input.obtainProducts()).thenThrow(ObtainFailedException.class);

        Controller controller = new Controller(input, output, logger);

        controller.select(getAllPassesFilter());

        verify(logger).setLevel("ERROR");
        verify(logger).log(anyString(), anyString());
    }

    @Test
    public void controllerDoesntSendAnythingToOutput_obtainProductsThrowsException() throws ObtainFailedException {
        Input input = mock(Input.class);
        Output output = mock(Output.class);
        Logger logger = mock(Logger.class);


        when(input.obtainProducts()).thenThrow(ObtainFailedException.class);

        Controller controller = new Controller(input, output, logger);

        controller.select(getAllPassesFilter());

        verify(output, never()).postSelectedProducts(any());
    }

    // region Helper methods

    private Collection<Product> getProducts() {
        List<Product> products = new ArrayList<>();
        Product product1 = new Product(1, "Prod1", Color.BLACK, BigDecimal.ONE);
        Product product2 = new Product(2, "Prod2", Color.RED, BigDecimal.ONE);
        Product product3 = new Product(3, "Prod3", Color.GREEN, BigDecimal.ONE);
        Product product4 = new Product(4, "Prod4", Color.GREEN, BigDecimal.ONE);

        products.add(product1);
        products.add(product2);
        products.add(product3);
        products.add(product4);

        return products;
    }

    private Filter<Product> getAllPassesFilter() {
        Filter<Product> filter = mock(Filter.class);
        when(filter.passes(any())).thenReturn(true);
        return filter;
    }

    private Filter<Product> getAllFailsFilter() {
        Filter<Product> filter = mock(Filter.class);
        when(filter.passes(any())).thenReturn(false);
        return filter;
    }


    /*
        Only returns true to product with id 2
     */
    private Filter<Product> getAllOnlyId2PassesFilter() {
        Filter<Product> filter = mock(Filter.class);
        when(filter.passes(Matchers.any(Product.class))).thenAnswer(invocationOnMock ->
                ((Product) invocationOnMock.getArguments()[0]).getId() == 2
        );
        return filter;
    }


    // endregion


}
