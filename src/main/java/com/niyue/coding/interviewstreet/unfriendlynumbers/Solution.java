package com.niyue.coding.interviewstreet.unfriendlynumbers;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Scanner;
import java.util.Set;

class Solution {
    private int N;
    private long K;
    private Map<Long, Set<Long>> factorsTree = new HashMap<Long, Set<Long>>();

    public static void main(String[] args) throws java.lang.Exception {
        Solution sl = new Solution();
        sl.solve();
    }

    public void solve() {
        List<Long> unfriendlyNumbers = getInput();
        
        Map<Long, Integer> primeFactorsCount = integerFactorization(K);
        
        Set<Long> factors = factors(primeFactorsCount);
        List<Long> orderedFactors = new ArrayList<Long>(factors);
        Collections.sort(orderedFactors); 
        Collections.reverse(orderedFactors);
        
        for(long factor : orderedFactors) {
            if(factors.contains(factor)) {
                boolean isDividable = isDividable(factor, unfriendlyNumbers);
                if(isDividable) {
                    Set<Long> childFactors = childFactors(factor, factorsTree);
                    factors.remove(factor);
                    factors.removeAll(childFactors);
                }
            }
        }
        System.out.println(factors.size());
    }
    
    private Map<Long, Integer> integerFactorization(long k) {
        int squareRootOfK = (int) Math.sqrt(K);
        // compute all primes less than squareRootOfK which potentially might be prime factors of K
        List<Long> primes = primeSieve(squareRootOfK);
        Map<Long, Integer> primeFactorsCount = primeFactorsCount(K, primes);
        // there might be at most one prime factor of K which is larger than squareRootOfK
        long equivalentK = equivalentNumber(primeFactorsCount);
        if(equivalentK != K) {
            primeFactorsCount.put(K/equivalentK, 1);
        }
        return primeFactorsCount;
    }
    
    private Set<Long> childFactors(long factor, Map<Long, Set<Long>> factorsTree) {
        Set<Long> childFactors = new HashSet<Long>();
        Set<Long> childFactorSet = factorsTree.get(factor);
        if(childFactorSet != null) {
            childFactors.addAll(childFactorSet);
            for(long childFactor : childFactorSet) {
                Set<Long> descendantFactorSet = childFactors(childFactor, factorsTree);
                childFactors.addAll(descendantFactorSet);
            }
        }
        return childFactors;
    }
    
    private boolean isDividable(long factor, List<Long> unfriendlyNumbers) {
        boolean isDividable = false;
        for(long number : unfriendlyNumbers) {
            if(number % factor == 0) {
                isDividable = true;
                break;
            }
        }
        return isDividable;
    }
    
    // all primes less than n
    private List<Long> primeSieve(int n) {
        boolean[] primeFlags = new boolean[n+1];
        Arrays.fill(primeFlags, true);
        
        int squareRoot = (int) Math.sqrt(n);
        for(int i=2;i<=squareRoot;i++) {
            if(primeFlags[i]) {
                for(int j=2;j<=n/i;j++) {
                    primeFlags[i*j] = false;
                }
            }
        }
        
        List<Long> primes = new ArrayList<Long>();
        for(long i=2;i<=n;i++) {
            if(primeFlags[(int)i]) {
                primes.add(i);
            }
        }
        return primes;
    }
    
    private Map<Long, Integer> primeFactorsCount(long n, List<Long> primes) {
        Map<Long, Integer> primeFactorsCount = new LinkedHashMap<Long, Integer>();
        for(long prime : primes) {
            while(n % prime == 0) {
                if(!primeFactorsCount.containsKey(prime)) {
                    primeFactorsCount.put(prime, 0);
                }
                int count = primeFactorsCount.get(prime) + 1;
                primeFactorsCount.put(prime, count);
                n = n / prime;
            }
            if(n == 1) {
                break;
            }
        }
        return primeFactorsCount;
    }
    
    private long equivalentNumber(Map<Long, Integer> primeFactorsCount) {
        long product = 1;
        for(Entry<Long, Integer> primeFactorCount : primeFactorsCount.entrySet()) {
            long prime = primeFactorCount.getKey();
            int count = primeFactorCount.getValue();
            product *= Math.pow(prime, count);
        }
        return product;
    }
    
    private Set<Long> factors(Map<Long, Integer> primeFactorsCount) {
        Set<Long> factors = new HashSet<Long>();
        if(!primeFactorsCount.isEmpty()) {
            for(Entry<Long, Integer> primeFactorCount : primeFactorsCount.entrySet()) {
                long prime = primeFactorCount.getKey();
                Map<Long, Integer> subPrimeFactorsCount = decreaseCount(primeFactorsCount, prime);
                Set<Long> subFactors = factors(subPrimeFactorsCount);
                factors.addAll(subFactors);
                for(Long factor : subFactors) {
                    long largeFactor = factor * prime; 
                    factors.add(largeFactor);
                    if(!factorsTree.containsKey(largeFactor)) {
                        factorsTree.put(largeFactor, new HashSet<Long>());
                    }
                    factorsTree.get(largeFactor).add(factor);
                }
            }
        }
        else {
            factors.add(1L);
        }
        return factors;
    }
    
    private Map<Long, Integer> decreaseCount(Map<Long, Integer> primeFactorsCount, long prime) {
        int count = primeFactorsCount.get(prime);
        Map<Long, Integer> subPrimeFactorsCount = new LinkedHashMap<Long, Integer>(primeFactorsCount);
        if(count > 1) {
            subPrimeFactorsCount.put(prime, count - 1);
        }
        else {
            subPrimeFactorsCount.remove(prime);
        }
        return subPrimeFactorsCount;
    }
    
    private List<Long> getInput() {
        Scanner scanner = new Scanner(System.in);
        N = scanner.nextInt();
        K = scanner.nextLong();
        List<Long> inputNumbers = new ArrayList<Long>(N);
        for (int i = 0; i < N; i++) {
            inputNumbers.add(scanner.nextLong());
        }
        return inputNumbers;
    }
}
