# Performance comparison between loops and streams
The primary goal here was to check whether using the Stream API could improve the performances of a program or not.

You can find below a list of benchmarks made to answer this question:

## BenchStreamVsListSum
Checks the performances of streams when performing a sum.
The values are stored in an ArrayList.

[Direct link](src/main/java/fr/rhiobet/benchmarks/BenchStreamVsListSum.java)

The results for a list of 50 000 000 Long values were the following:

| Method | Time (in µs) |
| --- | --- |
| Parallel stream | 92 956.735 ± 1 928.821 |
| For loop | 102 391.855 ± 2 115.295 |
| Foreach loop | 106 604.320 ± 2 061.707 |
| While iterator | 107 305.088 ± 1 547.746 |
| Normal stream | 108 654.906 ± 1 521.372 |

The results for a list of 1 000 000 Long values:

| Method | Time (in µs) |
| --- | --- |
| Parallel stream | 1 910.304 ± 72.229 |
| For loop | 2 461.561 ± 27.415 |
| While iterator | 2 521.699 ± 43,310 |
| Foreach loop | 2 612.577 ± 53.235 |
| Normal stream | 2 680.972 ± 98.532 |

The results for a list of 50 000 Long values:

| Method | Time (in µs) |
| --- | --- |
| For loop | 43.779 ± 1.731 |
| While iterator | 44.318 ± 6.374 |
| Foreach loop | 45.891 ± 6.374 |
| Normal stream | 59.358 ± 2.464 |
| Parallel stream | 141.418 ± 7.683 |

**Tentative conclusion:**

From these results, it looks like replacing a loop by a stream will always result in a loss of performances.
Parallel streams may give better results, but only when manipulating more than 100 000 elements.
Also, it appears that using a classic for loop instead of looping over an iterator gives slightly better results.

## BenchStreamIO
Checks the performances of streams when reading a file and filtering the results.
The file contains long values that will be parsed, and values greater than 900 will be stored in a list.
The returned value is the size of the list.

[Direct link](src/main/java/fr/rhiobet/benchmarks/BenchStreamIO.java)

The results for 100 000 000 elements were the following:

| Method | Time (in µs) |
| --- | --- |
| Parallel stream | 6 415 474.446 ± 245 596.921 |
| While iterator | 6 552 108.338 ± 515 001.281 |
| For iterator | 6 645 409.656 ± 561 622.522 |
| Normal stream | 7 327 062.237 ± 708 185.588 |

The results for 100 000 elements:

| Method | Time (in µs) |
| --- | --- |
| Parallel stream | 3 132.239 ± 109.645 |
| While iterator | 3 774.976 ± 1 015.061 |
| For iterator | 3 883.859 ± 1 379.047 |
| Normal stream | 3 916.220 ± 613.569 |

The results for 1 000 elements

| Method | Time (in µs) |
| --- | --- |
| While iterator | 47.936 ± 17.024 | 
| For iterator | 51.165 ± 20.193 |
| Parallel stream | X |
| Stream | X |

**Tentative conclusion:**

Again, even for I/O operations, it looks like using non parallel streams is definitely not the best way.
Parallel streams are at the top by far, but considering the error margin these results can't be trusted.

However, in the last benchmark (with 1 000 elements), using streams or parallel streams would always result in a FileSystemException "Too many open files"...
I don't know if this is a bug with the Stream API, but it is very concerning and should be investigated.

## BenchStreamMatrixMultiplication
Checks the performance of streams when computing a matrix to power 2.
The matrix is stored in a 2D array and there is no specific caching optimization.

[Direct link](src/main/java/fr/rhiobet/benchmarks/BenchStreamMatrixMultiplication.java)

The results for a 1000x1000 matrix were the following:

| Method | Time (in µs) |
| --- | --- |
| While loop | 2 224 413.742 ± 133 244.163 |
| For loop | 2 357 833.127 ±  140 901.426 |
| Parallel stream | 4 673 761.503 ±  137 148.827 |
| Normal stream | 7 322 371.598 ± 526 313.049 |

**Tentative conclusion**

In this benchmark, no specific cache optimization was done in the source code.
Despite that, we can see that streams, and even parallel streams, take way longer to complete the operation.
The only explanation I can think of is that, with the stream solution, the caching of the matrix is very minimal, leading to way more page faults than with the loop solutions.

## Best method depending on usage
This section covers which method is the most optimized to use in a specific situation, based on the results of this study.

| Situation | Method to use |
| --- | --- |
| Iterating through a list with more than 100 000 elements | Parallel stream |
| Iterating through a list with less than 100 000 elements | For loop using index |
| Reading file line by line | While with iterator |
| Doing matrix operations | Nested for loops |

Even if, when looking at the results, parallel streams are the best to read files line by line, the fact that they sometimes trigger exceptions makes me reluctant to put them here...

# Improving performances using the "final" keyword

The goal here was to see if declaring things as final could have some effects performance-wise.

## BenchFinalArray
Checks the performances when using the "final" keyword on different kinds of array variables.

[Direct link](src/main/java/fr/rhiobet/benchmarks/BenchFinalArray.java)

The results were the following:

| Kind | Time (in µs) |
| --- | --- |
| static final | 834 889.480 ±  54 030.044 |
| local | 1 051 233.608 ± 100 803.478 |
| local final | 1 057 387.611 ±  61 264.349 |
| non static final | 1 076 462.556 ±  56 087.109 |
| non static | 1 111 363.569 ±  42 588.345 |
| static | 1 119 872.915 ±  54 897.878 |

**Tentative conclusion**

It looks like making a variable "final" has little to no effect from a performance point of view, unless the variable is a static attribute (in which case it becomes an actual constant).

We can also notice that accessing local variables might be faster than accessing attributes.