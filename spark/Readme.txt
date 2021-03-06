################### Steps for building and execution ###########################
#using spark 2.2.0
#only the WordCount and PageRank algorithm are used for the analysis

#install maven
sudo apt install maven

#select hash function (in word count)
in WordCount/src/main/java/JavaWordCount.java
modify the selectedHash variable to the corresponding value of the HashType enum
the possible values can be seen in the HashType.java file of the hashes folder

#compile maven; generate .jar
mvn clean package

#submit the job from the job folder
spark-submit --class [PACKAGE_NAME].[ARTIFACT_ID] ./target/[FILENAME_]SNAPSHOT.jar

spark-submit --master local[5] --class JavaWordCount ./target/WordCount-1.0-SNAPSHOT.jar input

spark-submit --matser -local[5] --class JavaPageRank ./target/PageRank-1.0-SNAPSHOT.jar input 1
// 1 is iteration time

file iteration


##################### Additional Info ############################
#create a maven project
mvn archetype:generate -DgroupId=[PACKAGE_NAME] -DartifactId=[ARTIFACT_ID]

PACKAGE_NAME could be: org.apache.spark.examples
ARTIFACT_ID could be: JavaSparkPi


#modify dependencies to created pom.xml
  <properties>
    <project.build.sourceEncoding>UTF-8</project.build.sourceEncoding>
    <maven.compiler.source>1.8</maven.compiler.source>
    <maven.compiler.target>1.8</maven.compiler.target>
  </properties>

  <dependencies>
    <!-- Import Spark -->
    <dependency>
        <groupId>org.apache.spark</groupId>
        <artifactId>spark-core_2.11</artifactId>
        <version>2.2.0</version>
        <scope>provided</scope>
    </dependency>

    <dependency>
        <groupId>org.apache.spark</groupId>
        <artifactId>spark-sql_2.11</artifactId>
        <version>2.2.0</version>
        <scope>provided</scope>
    </dependency>

    <dependency>
      <groupId>junit</groupId>
      <artifactId>junit</artifactId>
      <version>3.8.1</version>
      <scope>test</scope>
    </dependency>
  </dependencies>
