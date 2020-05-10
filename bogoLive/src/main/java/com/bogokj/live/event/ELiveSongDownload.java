package com.bogokj.live.event;

import com.bogokj.live.model.LiveSongModel;


public class ELiveSongDownload {
	public LiveSongModel songModel;

	
	public ELiveSongDownload() {
		songModel = null;
	}
	
	public  ELiveSongDownload(LiveSongModel songModel) {
		this.songModel = songModel;
		
	}
}
