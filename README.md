Implementation of a sort-algorithm that mainly uses linear scaling as the sorting principle.
Best-case: O(n), Avg-case: O(n*log_n(range-of-value)), Worst-case: O(n^2)
In its most optimized form (-> "MoreCompact") linear additional memory is required.
ScalingSort works great on very large Arrays as it's runtime is mainly dependent on the range of values the array contains and not only on the length like classic sorting-algorithms.
Versions:
"BongoBongo" implements very first version of the scaling-sort idea.
"ScalingSort" implements a more memory-efficient/simpler and thus faster version of "BongoBongo".
"MoreCompact" implements a more compact version of ScalingSort with only linear additional memory.
