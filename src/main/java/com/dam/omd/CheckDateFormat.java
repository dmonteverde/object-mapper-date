package com.dam.omd;

import java.text.SimpleDateFormat;
import java.time.ZoneOffset;
import java.time.ZonedDateTime;
import java.util.Date;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;

import org.apache.commons.lang3.time.FastDateFormat;
import org.apache.commons.lang3.time.StopWatch;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class CheckDateFormat {

	private static int sampleSize = 10000; 
	private static int poolThreadSize = 10;

	private static final Logger log = LoggerFactory.getLogger(CheckDateFormat.class);
	 
    private static final SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSSz");
    private static final FastDateFormat fdf = FastDateFormat.getInstance("yyyy-MM-dd'T'HH:mm:ss.SSSz");

	public static void main( String[] args )
    {
		if( args.length > 0 ) {
			sampleSize = Integer.parseInt(args[0]);
		}
		if( args.length > 1 ) {
			poolThreadSize = Integer.parseInt(args[1]);
		}

		log.info("sample_size: {}", sampleSize);
		log.info("pool_thread_size: {}", poolThreadSize);
		 
    	invokeFormat("SimpleDateFormat (not Thread-safe)", false);
    	invokeFormat("FastDateFormat",  true );
    }
    
    
    public static void invokeFormat(String caseName, boolean useFastDateFormat)  {
    	
    	log.trace("start case: {}", caseName);
    	
        final ExecutorService executor = Executors.newFixedThreadPool(poolThreadSize);
        final AtomicInteger failedCount = new AtomicInteger();
        final AtomicInteger oKCount = new AtomicInteger();
        
        final StopWatch stopWatch = StopWatch.createStarted();
        final Date expectedResult1 = Date.from(ZonedDateTime.of(2020, 10, 24, 22, 50, 40, 194 * 1000 * 1000, ZoneOffset.UTC).toInstant());
        final Date expectedResult2 = Date.from(ZonedDateTime.of(2000, 12, 31, 11, 59, 59, 300 * 1000 * 1000, ZoneOffset.UTC).toInstant());
        for (int i = 1; i <= sampleSize; i++) {
            final Runnable runnable = () -> {
                final Date result;
                final Date expectedResult;
                if(System.currentTimeMillis() %2 == 0) { 
                    result = deserialize("2020-10-24T22:50:40.194UTC", useFastDateFormat);
                    expectedResult = expectedResult1;
                } else {
                    result = deserialize("2000-12-31T11:59:59.300UTC", useFastDateFormat); 
                    expectedResult = expectedResult2;
                }
                log.trace("date:{}", result);
                if(!expectedResult.equals(result)) {
                    failedCount.incrementAndGet();
                    log.trace("date is {} (expected {})", result, expectedResult);
                } else {
                	oKCount.incrementAndGet();
                }
            };
            executor.submit(runnable);
        }
        executor.shutdown();
        while (!executor.isTerminated()) {
        	// waiting end
        }
        
        final int total = oKCount.get() + failedCount.get();
        
		log.info("case: {} elapseTime: {} ms. Failed: {} of {} ({}%)", 
        		caseName, 
        		stopWatch.getTime(TimeUnit.MILLISECONDS), 
        		failedCount.get(), total,
        		failedCount.get() * 100.0 /  total);
    }

    private static Date deserialize(String jsonDate, boolean useFastDateFormat) {
        try {
            if (useFastDateFormat) {
                return fdf.parse(jsonDate);
            } else {
                return sdf.parse(jsonDate);
            }

        } catch (Exception e) {
            return null;
        }
    }
}
