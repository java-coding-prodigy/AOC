package aoc2019;

import org.apache.commons.math3.util.Pair;

import java.util.HashMap;
import java.util.Map;
import java.util.Scanner;
import java.util.concurrent.atomic.AtomicLong;

public class Day14 {
    public static void main(String[] args) {
        String[] lines = getInput();
        Map<Pair<Long, String>, Map<String, Long>> reactions = new HashMap<>();
        for (String line : lines) {
            String[] split = line.split(" => ");
            Scanner sc = new Scanner(split[1]);
            var rhs = new Pair<>(sc.nextLong(), sc.next());
            Map<String, Long> lhs = new HashMap<>();
            sc = new Scanner(split[0].replaceAll(",", ""));
            while (sc.hasNext()) {
                long n = sc.nextLong();
                lhs.put(sc.next(), n);
            }
            reactions.put(rhs, lhs);
        }
        Map<String, Long> available = new HashMap<>();
        available.put("ORE", 0L);
        reactions.forEach((k, v) -> available.put(k.getSecond(), 0L));
        AtomicLong oreNeeded = new AtomicLong();
        dfs("FUEL", 1, reactions, available, oreNeeded);
        long part1 = oreNeeded.get();
        System.out.println("Part 1: " + part1);
        long part2 = binarySearch(reactions);
        System.out.println("Part 2: " + part2);

    }

    private static long binarySearch(Map<Pair<Long, String>, Map<String, Long>> reactions) {
        long avail = 1000_000_000_000L;
        AtomicLong oreNeeded = new AtomicLong();
        Map<String, Long> available = new HashMap<>();
        long low = 0;
        long high = 5_000_000;
        while (low < high) {
            long mid = (low + high) / 2;
            oreNeeded.set(0);
            reactions.forEach((k, v) -> available.put(k.getSecond(), 0L));
            available.put("ORE", 0L);
            dfs("FUEL", mid, reactions, available, oreNeeded);

            switch (Long.compare(oreNeeded.get(), avail)) {
                case 1 -> high = mid - 1;
                case -1 -> low = mid + 1;
                case 0 -> low = mid;
            }
        }
        return low;
    }

    private static void dfs(String key, long amount,
            Map<Pair<Long, String>, Map<String, Long>> reactions, Map<String, Long> available,
            AtomicLong acc) {
        Map.Entry<Pair<Long, String>, Map<String, Long>> entry = reactions.entrySet()
                .stream()
                .filter(ent -> ent.getKey().getSecond().equals(key))
                .findAny()
                .orElseThrow();

        Pair<Long, String> output = entry.getKey();
        long made = available.get(output.getSecond());
        long multiplier = 0;

        while (made < amount) {
            made += output.getFirst();
            multiplier++;
        }

        for (var input : entry.getValue().entrySet()) {
            long needed = multiplier * input.getValue();
            if (input.getKey().equals("ORE")) {
                acc.addAndGet(needed);
            } else {
                dfs(input.getKey(), needed, reactions, available, acc);
                available.computeIfPresent(input.getKey(), (k, v) -> v - needed);
            }
        }

        available.put(key, made);
    }

    private static String[] getInput() {
        return """
                3 JQFM, 5 QMQB, 20 WQCT => 8 PHBMP
                2 XJFQR => 1 WQCT
                133 ORE => 3 KFKWH
                1 QGVJV, 9 TNRGW, 9 NSWDH => 5 HJPD
                4 QMQB, 2 QZMZ, 3 DQPX, 1 HJFV, 5 SLQN, 4 XHKG, 23 DBKL => 5 CVZLJ
                6 GFDP, 1 MXQF => 7 TDPN
                19 BWHKF, 2 KXHQW, 8 GHNG, 8 CSNS, 8 JVRQ, 1 PHBMP, 20 LZWR, 7 JKRZH => 5 PZRSQ
                1 JQFM => 1 QGVJV
                8 KFKWH => 7 ZJKB
                3 VMDSG, 2 BMSNV => 9 XJFQR
                7 ZKZHV => 6 DVRS
                1 WKFTZ, 5 SVTK, 1 QDJD => 7 JQFM
                19 FRTK => 2 QMTMN
                23 DVRS, 3 XNGTQ => 9 MCWF
                188 ORE => 3 HDRMK
                3 MCWF => 5 LHXN
                12 KFKWH, 2 DWBL => 8 ZKZHV
                2 GHNG => 8 SVTK
                4 MLJN, 9 CSNS => 6 DQPX
                2 NDNP, 1 LWGNJ, 6 DBKL, 3 RLKDF, 9 DQPX, 1 BWHKF => 3 JVGRC
                4 TNRGW => 2 CFBP
                2 KXHQW => 1 BWHKF
                7 HJFV => 4 PDKZ
                2 QZMZ => 5 BMSNV
                1 SVTK, 1 LZWR, 1 WQCT => 3 SLQN
                1 TDPN, 1 VMDSG => 7 NHVQD
                1 SVTK => 2 LZWR
                149 ORE => 9 DWBL
                1 XMHN, 1 PDKZ => 5 LWGNJ
                6 WCMV => 6 XNGTQ
                7 MCWF, 2 VCMPS => 1 HJFV
                11 BRTX, 37 CFBP, 2 HJPD, 72 HDRMK, 5 LWGNJ, 7 JVGRC, 3 CVZLJ, 8 PZRSQ, 3 LQBJP => 1 FUEL
                9 QMTMN, 14 FRTK, 14 HJFV => 9 NDNP
                1 KFKWH, 3 ZJKB => 9 MXQF
                1 HJFV, 1 XJFQR => 9 TNRGW
                1 DVRS => 2 BRTX
                4 QZMZ, 3 BMSNV, 3 GFDP => 6 VMDSG
                3 NHVQD => 6 WKFTZ
                1 BWHKF => 6 DBKL
                8 DWBL => 8 QZMZ
                4 MLJN, 16 NSWDH, 4 XHKG => 8 JVRQ
                2 DVRS, 32 XNGTQ, 9 MXQF => 7 GHNG
                1 DWBL => 8 WCMV
                8 SLQN, 1 CFBP => 9 MLJN
                1 QDJD => 4 XMHN
                3 BWHKF, 2 TNRGW => 9 XHKG
                1 WGLN => 8 GFDP
                1 MCWF, 1 XJFQR => 2 CSNS
                3 XNGTQ => 1 QDJD
                15 KXHQW, 3 WQCT, 2 QMTMN => 8 NSWDH
                9 XCMJ, 1 QMTMN => 2 JKRZH
                4 HDRMK => 4 WGLN
                9 NSWDH, 12 LHXN, 16 NDNP => 1 QMQB
                16 NHVQD, 3 DWBL, 1 WKFTZ => 4 FRTK
                1 GFDP => 2 VCMPS
                2 JQFM, 2 XMHN => 6 XCMJ
                1 DVRS, 19 QZMZ, 1 DWBL => 5 KXHQW
                1 QGVJV, 8 NDNP, 5 PDKZ => 1 RLKDF
                29 HJFV, 2 WKFTZ, 4 GFDP => 2 LQBJP""".split("\n");
    }


}
