package testUtils;

import org.meanbean.lang.Factory;
import java.util.UUID;

public class UUIDFactory implements Factory<UUID>
{
  @Override
  public UUID create()
  {
    return UUID.randomUUID();
  }
}