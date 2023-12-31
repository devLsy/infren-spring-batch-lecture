package com.dev.lsy.infrenspringbatchstudy.batch.chunk.processor;

import com.dev.lsy.infrenspringbatchstudy.batch.domain.Product;
import com.dev.lsy.infrenspringbatchstudy.batch.domain.ProductVo;
import org.modelmapper.ModelMapper;
import org.springframework.batch.item.ItemProcessor;

public class FileItemProcessor implements ItemProcessor<ProductVo, Product> {

    @Override
    public Product process(ProductVo item) throws Exception {

        ModelMapper modelMapper = new ModelMapper();
        Product product = modelMapper.map(item, Product.class);

        return product;
    }
}
