# MatrixLib ![maven](https://img.shields.io/github/v/release/Darrionat/MatrixLib)

An external Java library that can be utilized to perform calculations throughout matrices. This is useful when doing
calculations involving systems of linear equations, change of bases, multiplying matrices, and more. This project is
intended to be continually expanded.

## Maven ![maven](https://img.shields.io/github/v/release/Darrionat/MatrixLib)

To add this project to your Maven project make sure you have the following repository and dependency.

### Repository

```xml
<repository>
    <id>jitpack.io</id>
    <url>https://jitpack.io</url>
</repository>
```

### Dependency

```xml
<dependency>
    <groupId>com.github.darrionat</groupId>
    <artifactId>MatrixLib</artifactId>
    <version>version</version>
</dependency>
```

### Shading

```xml
<build>
    <plugins>
        <plugin>
            <groupId>org.apache.maven.plugins</groupId>
            <artifactId>maven-shade-plugin</artifactId>
            <version>3.1.0</version>
            <configuration>
                <relocations>
                    <relocation>
                        <pattern>me.darrionat.matrixlib</pattern>
                        <!-- Make sure to change the package below -->
                        <shadedPattern>my.plugin.utils</shadedPattern>
                    </relocation>
                </relocations>
            </configuration>
            <executions>
                <execution>
                    <phase>package</phase>
                    <goals>
                        <goal>shade</goal>
                    </goals>
                </execution>
            </executions>
        </plugin>
    </plugins>
</build>
```

## Documentation

Detailed information about the library will be coming soon to GitHub wiki, please use the JavaDocs until further notice.