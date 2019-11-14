# illumio
illumio coding assessment 
The Firewall class code is under /src

A.How I test it<br>
Once the implementation is done, first I manually write some rules in the csv file and test it with edge situations(port/ip at the edge of rule's range). This helps me to make sure that functionality of my code is correct. 
Then I write a rules generator to create 5,000 rules. Finally, I run my code again to see whether it could handle that number of rules and whether it is correct since there are duplicates when rules are many.
Based on my test, I think the functionality of the code is correct.<br><br>

B.Anything interesting<br>
For the design, first and foremost, I think that the time spending on query whether it should or should not pass firewall is the most important things. So I try to manage the query time to O(1) time complexity. My idea is to separate rule into 4 types based on direction and protocol, then put rest into HashMap. <br>
At the beginning, I wanted to put every port with ip into map but soon I realize it might not be possible because ip address has too many combinations. <br>
Instead, I put int value of port and string value of ip into HashMap. Also since there may be multiple ip rules for a single port, I put a list of ip string.<br>
When it comes to check whether accept the packet, first determine its type based on direction and protocol, then check port. If the port exists in the map, get the list of ip ranges, convert ip address to a long type value and compare them.
With this implementation I think both time and space are acceptable.<br><br>

C.Optimization<br>
If I have a little more time. First, I want to merge ip string in the list since there may be duplicates. Then I may stored ip addresses in an array and sort it, so I can use binary search when it comes to check whether ip is within the range.<br>
If I have much more time. Actually, I do not like current implementation since it is messy and not elegant enough. Also it lacks of scalability for protocol. I used to implement flow-based ECMP which combined 5 tuples of 104 bits to get a hash value. As for this problem, I think I may combine direction, protocol, port and ip into a byte array. Most importantly, I want to could implement a hash algorithm which gives a range of ip address same value. Finally, stored all hash value of rules in a HashSet, when it comes to check whether accept packet or not, simply compute hash value and see if there is a match in the HashSet.<br><br>

D.Beyonds
As have been studying computer science for years, I think that computer network could be my favorite part. Because it combines every aspect of computer science and really useful. This task is very interesting and practical, however, due to the time limitation, the implementation may not be so perfect. Still, I do find self-motivation and excitedness of solving such a problem.<br><br>

If there is a future opportunity, the rank of team I interested in would be: Platform > Data > Policy.<br><br><br><br>



Thank you!<br>
Best,<br>
Minghao Liu<br>
