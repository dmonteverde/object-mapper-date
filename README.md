# object-mapper-date
#jackson object-mapper custom SimpleDateFormat vs FastDateFormat Benchmark


## Tecnologia
maven 
j11


## Definitions
SDF=SimpleDateFormat
FDF=FastDateFormat

## use

mvn install
./target/omd-jar-with-dependencies.jar

> ✔ ~/dev/git/object-mapper-date [main|✔]
> 16:35 $ java -jar ./target/omd-jar-with-dependencies.jar
> 16:38:19.548 [main] INFO com.dam.omd.CheckObjectMapper - sample_size: 10000
> 16:38:19.553 [main] INFO com.dam.omd.CheckObjectMapper - pool_thread_size: 10
> 16:38:21.111 [main] INFO com.dam.omd.CheckObjectMapper - case: SDF*********************************************** elapseTime: 894 ms. Failed: 0 of 10000 (0.0%)
> 16:38:21.526 [main] INFO com.dam.omd.CheckObjectMapper - case: SDF by WRAPPER - NOT CLONE (not thread-safe!)***** elapseTime: 413 ms. Failed: 2603 of 10000 (26.03%)
> 16:38:22.209 [main] INFO com.dam.omd.CheckObjectMapper - case: SDF by WRAPPER - FULL CLONE*********************** elapseTime: 681 ms. Failed: 0 of 10000 (0.0%)
> 16:38:22.479 [main] INFO com.dam.omd.CheckObjectMapper - case: FDF by WRAPPER************************************ elapseTime: 270 ms. Failed: 0 of 10000 (0.0%)
> 16:38:22.617 [main] INFO com.dam.omd.CheckObjectMapper - case: withCustomDateDeserializer (fdf)****************** elapseTime: 136 ms. Failed: 0 of 10000 (0.0%)


 ./target/omd-jar-with-dependencies.jar {sample_size} {pool_thread_size}

> ✔ ~/dev/git/object-mapper-date [main|✔]
> 16:35 $ java -jar ./target/omd-jar-with-dependencies.jar 10000 50
> 16:35:49.889 [main] INFO com.dam.omd.CheckObjectMapper - sample_size: 10000
> 16:35:49.906 [main] INFO com.dam.omd.CheckObjectMapper - pool_thread_size: 50
> 16:35:51.823 [main] INFO com.dam.omd.CheckObjectMapper - case: SDF*********************************************** elapseTime: 1041 ms. Failed: 0 of 10000 (0.0%)
> 16:35:52.737 [main] INFO com.dam.omd.CheckObjectMapper - case: SDF by WRAPPER - NOT CLONE (not thread-safe!)***** elapseTime: 913 ms. Failed: 4202 of 10000 (42.02%)
> 16:35:54.810 [main] INFO com.dam.omd.CheckObjectMapper - case: SDF by WRAPPER - FULL CLONE*********************** elapseTime: 2071 ms. Failed: 0 of 10000 (0.0%)
> 16:35:55.030 [main] INFO com.dam.omd.CheckObjectMapper - case: FDF by WRAPPER************************************ elapseTime: 219 ms. Failed: 0 of 10000 (0.0%)
> 16:35:55.210 [main] INFO com.dam.omd.CheckObjectMapper - case: withCustomDateDeserializer (fdf)****************** elapseTime: 179 ms. Failed: 0 of 10000 (0.0%)
