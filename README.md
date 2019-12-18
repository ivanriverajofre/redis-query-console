# redis-query-console
Lightweight command line application that query a key in Redis and shows the content as a char sequence. Useful  when there is no redis-cli available and you need to know if a key is in a cache or not.

# How to build
mvn clean compile package

# How to use
To query in a standalone Redis server, in localhost port 6379:

	java -jar redis-query-console-1.0.0.jar localhost:6379 Customer::1
	Using Redis host(s):localhost:6379
	Querying: Customer::1
	Result:
	{"id":1, "name":"Iván Rivera"}

To query in a Redis cluster with two nodes:
	java -jar redis-query-console-1.0.0.jar node1:6379,node2:6379 Customer::1
	Using Redis host(s):node1:6379,node2:6379
	Querying: Customer::1
	Result:
	{"id":1, "name":"Iván Rivera"}

To query in a Redis cluster with two nodes and a password:
	java -jar redis-query-console-1.0.0.jar node1:6379,node2:6379 mypassword Customer::1
	Using Redis host(s):node1:6379,node2:6379
	Using password:mypassword
	Querying: Customer::1
	Result:
	{"id":1, "name":"Iván Rivera"}

