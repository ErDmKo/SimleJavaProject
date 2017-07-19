package tk.erdmko;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.BitSet;
import java.util.Collections;
import java.util.List;
import java.util.Scanner;
import java.util.stream.Collectors;

public class Solution {

    private static int diffSize = 15;
    private static int step = 50;

    private static long[] toInts(BitSet bitList) {
        int size = bitList.length();
        ArrayList<Integer> out = new ArrayList<Integer>();
        Boolean lastBit = null;
        int j = 0; // int counter
        for (int i = size - 1; i >= 0; i--) {
            boolean bit = bitList.get(i);
            if (lastBit == null) {
                j++;
                if (size == 1) {
                    out.add(j);
                }
            } else if (lastBit == bit) {
                j++;
            } else {
                out.add(j);
                j = 1;
            }
            if (i == 0) {
                out.add(j);
            }
            lastBit = bit;
        }
        return out.stream().mapToLong(num -> num).toArray();
    }
    private static BitSet toBits(int[] intList) {
        BitSet out = new BitSet();
        int shift = 0;
        for (int i = intList.length - 1; i >= 0 ; i--) {
            if (i % 2 == 0) {
                for (int j = 0; j < intList[i]; j++) {
                    out.set(shift + j);
                }
            }
            shift += intList[i];
        }
        return out;
    }
    private static BitSet nextBit(BitSet binary, int startBit) {
        int firstZero = binary.nextClearBit(startBit);

        if (binary.get(firstZero - 1)) {
            int length = binary.length();
            binary.flip(firstZero - 1, firstZero + 1);

            if (!binary.get(0)) {
                int firstOne = binary.nextSetBit(0);
                if (firstOne == 1 + startBit) {
                    return binary;
                }
                firstZero = binary.nextClearBit(firstOne);
                binary.set(firstZero - firstOne, firstZero, false);
                binary.set(0, firstZero - firstOne, true);
                return binary;
            } else {
                return binary;
            }
        } else {
            return Solution.nextBit(binary, firstZero + 1);
        }

    }
    private static class Out {
        Integer[] out;
        Long[] unpack;
        Out(Integer[] normalized, Long[] unpackEd) {
            out = normalized;
            unpack = unpackEd;
        }
    }
    private static Solution.Out normalize(long[] numList) {
        Integer[] out = new Integer[numList.length];
        List <Long> copy = Arrays.stream(numList.clone())
            .filter(n -> n > Solution.diffSize)
            .boxed()
            .collect(Collectors.toList());
        Collections.sort(copy, Collections.reverseOrder());
        ArrayList<Long> unpackList = new ArrayList<Long>();
        for (int i = 0; i < numList.length; i++) {
            if (numList[i] > Solution.diffSize) {
                int index = copy.indexOf(numList[i]);
                out[i] = Solution.diffSize
                    + (Solution.step + (copy.size() - index) * Solution.step);
                unpackList.add(numList[i]);
            } else {
                out[i] = Math.toIntExact(numList[i]);
            }
        }
        Collections.sort(unpackList, Collections.reverseOrder());
        return new Solution.Out(
            out,
            unpackList.toArray(new Long[unpackList.size()])
        );
    }

    public static void main(String[] args) {
        Scanner in = new Scanner(System.in);
        int testCasesNumber = in.nextInt();
        Solution st = new Solution();
        for (int i = 0; i < testCasesNumber; i++) {
            int size = in.nextInt();
            long[] encodedNumber = new long[size];
            for (int j = 0; j < size; j++) {
                encodedNumber[j] = in.nextLong();
            }
            Solution.Out diffEncodedNumber = Solution.normalize(encodedNumber);
            BitSet binary = Solution.toBits(
                    Arrays.stream(diffEncodedNumber.out).mapToInt(Integer::intValue).toArray());
            binary = Solution.nextBit(binary, binary.nextSetBit(0));
            long[] outNumbers = Solution.toInts(binary);
            ArrayList<Long> newNumbers = new ArrayList<Long>();
            for (long outNumber : outNumbers) {
                if (outNumber >= Solution.diffSize) {
                    newNumbers.add(outNumber);
                }
            }
            Collections.sort(newNumbers, Collections.reverseOrder());
            for (int k = 0; k < outNumbers.length; k++) {
                if (outNumbers[k] >= Solution.diffSize) {
                    int index = newNumbers.indexOf(outNumbers[k]);

                    
                    long previousBigValue = 
                        diffEncodedNumber.unpack[index];
                    int trueIndex = Arrays.asList(diffEncodedNumber.unpack).indexOf(previousBigValue);

                    long previousEncodedValue  = 0L;

                    previousEncodedValue = Solution.diffSize + Solution.step + ((newNumbers.size() - trueIndex) * Solution.step);
                    outNumbers[k] = previousBigValue + outNumbers[k] - previousEncodedValue;

                }
            }
            System.out.println(outNumbers.length);
            System.out.println(Arrays.stream(outNumbers)
                .mapToObj(String::valueOf)
                .collect(Collectors.joining(" ")));
        }
    }
}
