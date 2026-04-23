package atest;



import java.util.function.Supplier;

import io.hypersistence.tsid.TSID;

public class CustomTsidSupplier implements Supplier<TSID.Factory> {

    @Override
    public TSID.Factory get() {
        return TSID.Factory.builder()
            .withNodeBits(10)
            .build();
    }
}