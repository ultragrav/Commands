# Commands
A simple commands library for Spigot and Bungeecord

## Installation
### Maven:
```xml
<repository>
    <id>UltraGrav</id>
    <url>https://mvn.ultragrav.net</url>
</repository>

<dependency>
    <groupId>net.ultragrav</groupId>
    <artifactId>Commands</artifactId>
    <version>1.5.0</version>
</dependency>
```

## Example Usage
ExampleCommand.java
```java
import net.ultragrav.command.UltraCommand;

public class ExampleCommand extends UltraCommand {
    public ExampleCommand() {
        this.addAlias("example");

        this.addChildren(
                new ExampleSubCommand()
        );
    }
}
```

ExampleSubCommand.java

```java
import net.ultragrav.command.Parameter;
import net.ultragrav.command.UltraCommand;
import net.ultragrav.command.provider.impl.StringProvider;

import java.util.List;

public class ExampleSubCommand extends UltraCommand {
    public ExampleSubCommand() {
        this.addAlias("sub");

        this.setAllowConsole(false);

        this.addParameter(Parameter.builder(StringProvider.getInstance()).name("message").varArg(true).build());
    }

    @Override
    protected void perform() {
        List<String> message = getArgument(0);

        tell(String.join(" ", message));
    }
}
```

How to register:
```java
new ExampleCommand().register();
```


Full documentation coming soon
