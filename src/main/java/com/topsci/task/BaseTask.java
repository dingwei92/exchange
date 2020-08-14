package com.topsci.task;

import com.topsci.common.CatchAbout;
import com.topsci.db.service.YwDBService;

public abstract class BaseTask implements Runnable{

	public abstract void run();
	protected YwDBService ywDBService = YwDBService.getInstance();
	protected CatchAbout catchAbout = CatchAbout.getInstance();

}
