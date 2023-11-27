public class Main {
    public static void main (String[] args) {
        System.out.println("=========== OPTIMISTIC ============");
        optimistic();
        System.out.println("===================================");

        System.out.println("\n");

        System.out.println("=========== PESSIMISTIC ===========");
        pessimistic();
        System.out.println("===================================");
    }

    public static void optimistic () {
        TransactionalMapOptimistic<String, String> transactionalMap = new TransactionalMapOptimistic<>();

        test(transactionalMap);
    }

    public static void pessimistic () {
        TransactionalMapPessimistic<String, String> transactionalMap = new TransactionalMapPessimistic<>();

        test(transactionalMap);
    }

    private static void test (BaseTransactionalHashMap<String, String> object) {
        object.put("key1", "value0");
        System.out.println(object.get("key1"));

        object.beginTransaction("key1");
        object.put("key1", "value1");
        System.out.println(object.get("key1"));

        object.rollbackTransaction("key1");
        System.out.println(object.get("key1"));
    }
}
