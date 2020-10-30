package com.dam.omd;

import java.io.IOException;
import java.text.DateFormat;
import java.text.FieldPosition;
import java.text.ParseException;
import java.text.ParsePosition;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.LinkedHashMap;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.time.FastDateFormat;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.deser.std.StdDeserializer;
import com.fasterxml.jackson.databind.module.SimpleModule;

public class ObjectMapperCatalog {

    private final SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSSz");
    private final FastDateFormat fdf = FastDateFormat.getInstance("yyyy-MM-dd'T'HH:mm:ss.SSSz");

    private Map<String, ObjectMapper> objectMappers = new LinkedHashMap<>(); 
    {
        objectMappers.put("SDF", new ObjectMapper()
        			.setDateFormat(sdf));
        
        objectMappers.put("SDF by WRAPPER - NOT CLONE (not thread-safe!)", 
        		new ObjectMapper()
        			.setDateFormat(new SupportedDatesDateFormat(sdf)));
        
        objectMappers.put("SDF by WRAPPER - FULL CLONE", 
        		new ObjectMapper()
        			.setDateFormat(new SupportedDatesDateFormatFullClone(sdf)));
        
        objectMappers.put("FDF by WRAPPER", 
        		new ObjectMapper()
        			.setDateFormat(new SupportedDatesDateFormatFDF(fdf)));
        
        objectMappers.put("withCustomDateDeserializer (fdf)", createObjectMapperWithCustomDateDeserializer());
    }

    
    public Map<String, ObjectMapper> getObjectMappers() {
		return objectMappers;
	}

	private  ObjectMapper createObjectMapperWithCustomDateDeserializer() {
        final ObjectMapper mapper = new ObjectMapper();
        final SimpleModule customDateModule = new SimpleModule();
        customDateModule.addDeserializer(Date.class, new CustomDateDeserializer());
        mapper.registerModule(customDateModule);
        return mapper;
    }
    
    private class CustomDateDeserializer extends StdDeserializer<Date> {

        private static final long serialVersionUID = 1L;

        CustomDateDeserializer() {
                super(Date.class);
        }

        @Override
        public Date deserialize(JsonParser jp, DeserializationContext ctxt) throws IOException {
            final String valueAsString = jp.getValueAsString();
            try {                                                                                       
                return valueAsString == null ? null : fdf.parse(valueAsString);
            } catch (final ParseException pe) {
                throw new IOException("Can not deserialize: " + valueAsString, pe); 
            }
        }
    }
    
  
    public class SupportedDatesDateFormat extends DateFormat {
        private static final long serialVersionUID = 1;

        private final SimpleDateFormat formatter;

        public SupportedDatesDateFormat(SimpleDateFormat formatter) {
            this.formatter = formatter;
        }

        @Override
        public StringBuffer format(Date date, StringBuffer stringBuffer, FieldPosition fieldPosition) {
            return this.formatter.format(date, stringBuffer, fieldPosition);
        }

        @Override
        public Date parse(String s, ParsePosition parsePosition) {

            try {
                Date d = this.formatter.parse(s);
                parsePosition.setIndex(parsePosition.getIndex() + s.length());
                return d;
            } catch (Exception ex) {
                throw new IllegalArgumentException(String.format("Can't parse %s as Date",s), ex);
            }
        }

        @Override
        public Object clone() {
            return new SupportedDatesDateFormat(this.formatter);
        }
    }
    
    public class SupportedDatesDateFormatFullClone extends DateFormat {
        private static final long serialVersionUID = 1;

        private final SimpleDateFormat formatter;

        public SupportedDatesDateFormatFullClone(SimpleDateFormat formatter) {
            this.formatter = formatter;
        }

        @Override
        public StringBuffer format(Date date, StringBuffer stringBuffer, FieldPosition fieldPosition) {
            return this.formatter.format(date, stringBuffer, fieldPosition);
        }

        @Override
        public Date parse(String s, ParsePosition parsePosition) {

            try {
                Date d = this.formatter.parse(s);
                parsePosition.setIndex(parsePosition.getIndex() + s.length());
                return d;
            } catch (Exception ex) {
                throw new IllegalArgumentException(String.format("Can't parse %s as Date",s), ex);
            }
        }

        @Override
        public Object clone() {
            return new SupportedDatesDateFormatFullClone((SimpleDateFormat) this.formatter.clone());
        }
    }
    
    public class SupportedDatesDateFormatFDF extends DateFormat {
        private static final long serialVersionUID = 1;

        private final FastDateFormat formatter;

        public SupportedDatesDateFormatFDF(FastDateFormat formatter) {
            this.formatter = formatter;
        }

        @Override
        public StringBuffer format(Date date, StringBuffer stringBuffer, FieldPosition fieldPosition) {
            return this.formatter.format(date, stringBuffer, fieldPosition);
        }

        @Override
        public Date parse(String s, ParsePosition parsePosition) {

            try {
                Date d = this.formatter.parse(s);
                parsePosition.setIndex(parsePosition.getIndex() + s.length());
                return d;
            } catch (Exception ex) {
                throw new IllegalArgumentException(String.format("Can't parse %s as Date",s), ex);
            }
        }

        @Override
        public Object clone() {
            return new SupportedDatesDateFormatFDF(this.formatter);
        }
    }

}
