package com.rune.utils.easyExcel;

import com.alibaba.excel.converters.Converter;
import com.alibaba.excel.metadata.GlobalConfiguration;
import com.alibaba.excel.metadata.data.WriteCellData;
import com.alibaba.excel.metadata.property.ExcelContentProperty;
import org.apache.commons.lang3.time.DateFormatUtils;

import java.sql.Timestamp;

/**
 * @author wyp
 * @date 22/9/13 15:01
 * @description 日期转换
 */
public class ConverterDate implements Converter<Timestamp> {

    @Override
    public WriteCellData<String> convertToExcelData(Timestamp timestamp, ExcelContentProperty contentProperty,
                                                    GlobalConfiguration globalConfiguration) {
        return new WriteCellData<>(DateFormatUtils.format(timestamp, "yyyy-MM-dd HH:mm:ss"));
    }
}
