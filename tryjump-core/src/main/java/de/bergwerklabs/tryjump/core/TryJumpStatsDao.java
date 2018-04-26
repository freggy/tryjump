package de.bergwerklabs.tryjump.core;

import de.bergwerklabs.framework.bedrock.api.PlayerdataDao;

/**
 * Created by Yannic Rieger on 11.02.2018.
 *
 * <p>
 *
 * @author Yannic Rieger
 */
public class TryJumpStatsDao {

  private PlayerdataDao dao;

  public TryJumpStatsDao(PlayerdataDao dao) {
    this.dao = dao;
  }
}
