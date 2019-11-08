import java.util.*;
import com.amazonaws.regions.*;
import com.amazonaws.services.dynamodbv2.*;
import com.amazonaws.services.dynamodbv2.model.*;
import com.amazonaws.services.dynamodbv2.document.*;
import com.amazonaws.services.dynamodbv2.datamodeling.*;

public class DDB_Latency {
    public AmazonDynamoDB client;
    public String region;
    public String table;

    public DDB_Latency(String region, String table) {
        this.region = region;
        this.table = table;
    }

    public void test(int loopCnt) {
        this.client = AmazonDynamoDBClientBuilder.standard()
        .withRegion(this.region)
        .build();

        for (int i=0; i<loopCnt; i++) {
            put(client);
        }
    }

    public void put(AmazonDynamoDB client) {
        try {
            Random r = new Random();
            String hash = UUID.randomUUID().toString();
            int sort = r.nextInt(1000000);
            String value = hash + "-" + sort;

            HashMap<String, AttributeValue> item = new HashMap<String, AttributeValue>();
            item.put("hash", new AttributeValue(hash));
            item.put("sort", new AttributeValue().withN(Integer.toString(sort)));
            item.put("val", new AttributeValue(value));
            PutItemRequest putItemRequest = new PutItemRequest().withTableName(this.table).withItem(item);

            long t0 = System.currentTimeMillis();
            client.putItem(putItemRequest);
            long t1 = System.currentTimeMillis();
            long latency = t1 - t0;
            System.out.println("Latency: " + latency);
        } catch (Exception e) {
            System.out.println(e.getMessage());
            e.printStackTrace();
        }
    }

    public static void main(String[] args) {
        if (args.length != 3) {
            System.out.println("Syntax: javac DDB_Latency <region> <table> <loops>");
            return;
        }

        try {
            String region = args[0];
            String table = args[1];
            int loopCnt = Integer.parseInt(args[2]);
            DDB_Latency test = new DDB_Latency(region, table);
            test.test(loopCnt);
        } catch (Exception e) {
            System.out.println(e.getMessage());
            e.printStackTrace();
        }
    }
}
