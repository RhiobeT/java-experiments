# Benchmarks made with JMH

## BenchStreamVsListSum
Checks the performance of streams when performing a sum.
The values are stored in an ArrayList.

The results for a 50000000 list of long values were the following:

| Method | Time (in µs) |
| --- | --- |
| Parallel stream | 108801,755 ± 2864,195 |
| For loop | 126789,251 ± 5656,977 |
| Foreach loop | 133190,942 ± 7615,004 |
| Normal stream | 139930,717 ± 9172,286 |

## BenchStreamIO
Checks the performance of streams when reading a file and filtering the results.
The file contains long values that will be parsed, and values greater than 900 will be stored in a list.
The returned value is the size of the list.

The results for 100000000 elements were the following:

| Method | Time (in µs) |
| --- | --- |
| Parallel stream | 7958808,077 ± 241437,777 |
| While iterator | 8018541,814 ± 323836,240 |
| Normal stream | 8529013,290 ± 304520,692 |

## BenchStreamMatrixMultiplication
Checks the performance of streams when computing a matrix to power 2.
The matrix is stored in a 2D array and there is no specific caching optimization.

The results for a 1000x1000 matrix were the following:

| Method | Time (in µs) |
| --- | --- |
| For loop | 2831629,361 ± 345859,847 |
| Parallel stream | 4834140,698 ±  66740,696 |
| Normal stream | 7322371,598 ± 526313,049 |

## BenchFinalArray
Checks the performances when using the "final" keyword on different kinds of array variables.

The results were the following:

| Kind | Time (in µs) |
| --- | --- |
| static final | 834889,480 ±  54030,044 |
| local | 1051233,608 ± 100803,478 |
| local final | 1057387,611 ±  61264,349 |
| non static final | 1076462,556 ±  56087,109 |
| non static | 1111363,569 ±  42588,345 |
| static | 1119872,915 ±  54897,878 |