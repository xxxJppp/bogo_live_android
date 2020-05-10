package com.bogokj.live.event;

import com.bogokj.live.model.LiveSongModel;

public class EPlayMusic
{
	public LiveSongModel songModel;
	public EPlayMusic(LiveSongModel model) {
		songModel = model;
	}
	public EPlayMusic() {
		songModel = null;
	}
}
