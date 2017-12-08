import java.nio.file.*;

public class Throughput { 
    public static void main(String[] args) throws Exception {
        int runs = 10;
        String inputFile = args[0];
        int hashTimes = Integer.valueOf(args[1]);
        Path path = Paths.get(inputFile);
        byte[] data = Files.readAllBytes(path);

        System.out.println("Bytes read: " + data.length);

        HashType type = HashType.NONE;
        long start = 0;
        long end = 0;
        long avg = 0;
        long total = 0;
        //run once to load data
        HashCode.getHash(data, type);  

        //type
        type = HashType.NONE;
        System.out.println("============");
        System.out.println("Running Hash " + type);
        total = 0;
        for(int i = 0; i < runs; i++) {
            for (int k = 0; k < hashTimes; k++) {
                start = System.currentTimeMillis();
                HashCode.getHash(data, type);  
                end = System.currentTimeMillis();
                total += end - start;
            }
        } 
        avg = total / runs;
        System.out.println("avg: " + avg);

        //HashType.JENKINS
        type = HashType.JENKINS;
        System.out.println("============");
        System.out.println("Running Hash " + type);
        total = 0;
        for(int i = 0; i < runs; i++) {
            for (int k = 0; k < hashTimes; k++) {
                start = System.currentTimeMillis();
                HashCode.getHash(data, type);  
                end = System.currentTimeMillis();
                total += end - start;
            }
        } 
        avg = total / runs;
        System.out.println("avg: " + avg);

        //HashType.FLETCHER32
        type = HashType.FLETCHER32;
        System.out.println("============");
        System.out.println("Running Hash " + type);
        total = 0;
        for(int i = 0; i < runs; i++) {
            for (int k = 0; k < hashTimes; k++) {
                start = System.currentTimeMillis();
                HashCode.getHash(data, type);  
                end = System.currentTimeMillis();
                total += end - start;
            }
        } 
        avg = total / runs;
        System.out.println("avg: " + avg);

        //HashType.BLAKE_2S
        type = HashType.BLAKE_2S;
        System.out.println("============");
        System.out.println("Running Hash " + type);
        total = 0;
        for(int i = 0; i < runs; i++) {
            for (int k = 0; k < hashTimes; k++) {
                start = System.currentTimeMillis();
                HashCode.getHash(data, type);  
                end = System.currentTimeMillis();
                total += end - start;
            }
        } 
        avg = total / runs;
        System.out.println("avg: " + avg);

        //HashType.XX_HASH
        type = HashType.XX_HASH;
        System.out.println("============");
        System.out.println("Running Hash " + type);
        total = 0;
        for(int i = 0; i < runs; i++) {
            for (int k = 0; k < hashTimes; k++) {
                start = System.currentTimeMillis();
                HashCode.getHash(data, type);  
                end = System.currentTimeMillis();
                total += end - start;
            }
        } 
        avg = total / runs;
        System.out.println("avg: " + avg);


        //HashType.SIM_HASH
        type = HashType.SIM_HASH;
        System.out.println("============");
        System.out.println("Running Hash " + type);
        total = 0;
        for(int i = 0; i < runs; i++) {
            for (int k = 0; k < hashTimes; k++) {
                start = System.currentTimeMillis();
                HashCode.getHash(data, type);  
                end = System.currentTimeMillis();
                total += end - start;
            }
        } 
        avg = total / runs;
        System.out.println("avg: " + avg);


        //HashType.ADLER32
        type = HashType.ADLER32;
        System.out.println("============");
        System.out.println("Running Hash " + type);
        total = 0;
        for(int i = 0; i < runs; i++) {
            for (int k = 0; k < hashTimes; k++) {
                start = System.currentTimeMillis();
                HashCode.getHash(data, type);  
                end = System.currentTimeMillis();
                total += end - start;
            }
        } 
        avg = total / runs;
        System.out.println("avg: " + avg);

        //HashType.XOR8
        type = HashType.XOR8;
        System.out.println("============");
        System.out.println("Running Hash " + type);
        total = 0;
        for(int i = 0; i < runs; i++) {
            for (int k = 0; k < hashTimes; k++) {
                start = System.currentTimeMillis();
                HashCode.getHash(data, type);  
                end = System.currentTimeMillis();
                total += end - start;
            }
        } 
        avg = total / runs;
        System.out.println("avg: " + avg);

        //HashType.CRC32
        type = HashType.CRC32;
        System.out.println("============");
        System.out.println("Running Hash " + type);
        total = 0;
        for(int i = 0; i < runs; i++) {
            for (int k = 0; k < hashTimes; k++) {
                start = System.currentTimeMillis();
                HashCode.getHash(data, type);  
                end = System.currentTimeMillis();
                total += end - start;
            }
        } 
        avg = total / runs;
        System.out.println("avg: " + avg);

        //HashType.MURMUR
        type = HashType.MURMUR;
        System.out.println("============");
        System.out.println("Running Hash " + type);
        total = 0;
        for(int i = 0; i < runs; i++) {
            for (int k = 0; k < hashTimes; k++) {
                start = System.currentTimeMillis();
                HashCode.getHash(data, type);  
                end = System.currentTimeMillis();
                total += end - start;
            }
        } 
        avg = total / runs;
        System.out.println("avg: " + avg);

        //HashType.WHIRLPOOL
        type = HashType.WHIRLPOOL;
        System.out.println("============");
        System.out.println("Running Hash " + type);
        total = 0;
        for(int i = 0; i < runs; i++) {
            for (int k = 0; k < hashTimes; k++) {
                start = System.currentTimeMillis();
                HashCode.getHash(data, type);  
                end = System.currentTimeMillis();
                total += end - start;
            }
        } 
        avg = total / runs;
        System.out.println("avg: " + avg);

        //HashType.SHA256
        type = HashType.SHA256;
        System.out.println("============");
        System.out.println("Running Hash " + type);
        total = 0;
        for(int i = 0; i < runs; i++) {
            for (int k = 0; k < hashTimes; k++) {
                start = System.currentTimeMillis();
                HashCode.getHash(data, type);  
                end = System.currentTimeMillis();
                total += end - start;
            }
        } 
        avg = total / runs;
        System.out.println("avg: " + avg);

        //HashType.MD5
        type = HashType.MD5;
        System.out.println("============");
        System.out.println("Running Hash " + type);
        total = 0;
        for(int i = 0; i < runs; i++) {
            for (int k = 0; k < hashTimes; k++) {
                start = System.currentTimeMillis();
                HashCode.getHash(data, type);  
                end = System.currentTimeMillis();
                total += end - start;
            }
        } 
        avg = total / runs;
        System.out.println("avg: " + avg);

        //HashType.HASH_CODE
        type = HashType.HASH_CODE;
        System.out.println("============");
        System.out.println("Running Hash " + type);
        total = 0;
        for(int i = 0; i < runs; i++) {
            for (int k = 0; k < hashTimes; k++) {
                start = System.currentTimeMillis();
                HashCode.getHash(data, type);  
                end = System.currentTimeMillis();
                total += end - start;
            }
        } 
        avg = total / runs;
        System.out.println("avg: " + avg);

            
    }
}
