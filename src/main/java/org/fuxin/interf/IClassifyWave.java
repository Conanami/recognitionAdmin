package org.fuxin.interf;

import org.fuxin.caller.StandFile;
import org.fuxin.caller.WaveFileResult;

import java.io.File;
import java.util.ArrayList;

public interface IClassifyWave {
	public WaveFileResult Filter(File file, ArrayList<StandFile> standlist);
}
