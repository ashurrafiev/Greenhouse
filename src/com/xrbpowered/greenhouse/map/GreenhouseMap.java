package com.xrbpowered.greenhouse.map;

import java.awt.Color;
import java.awt.Graphics2D;
import java.util.ArrayList;

import com.xrbpowered.gl.res.textures.BufferTexture;
import com.xrbpowered.gl.scene.StaticMeshActor;
import com.xrbpowered.greenhouse.render.PrefabActor;
import com.xrbpowered.greenhouse.render.PrefabComponent;
import com.xrbpowered.greenhouse.render.TileActor;

public class GreenhouseMap {

	public final int sizex, sizey;
	private TileActor[][] tiles;
	
	public ArrayList<StaticMeshActor> crystalActors = new ArrayList<>();
	public int collectedCrystals = 0;

//	private ArrayList<TileActor> tileList = new ArrayList<>();

	public GreenhouseMap(int sizex, int sizey) {
		this.sizex = sizex;
		this.sizey = sizey;
		this.tiles = new TileActor[sizex+1][sizey+1];
	}
	
	public void addTile(int x, int y, TileActor tile) {
/*		if(tiles[x][y]!=null)
			tileList.remove(tiles[x][y]);
		tileList.add(tile);*/
		tiles[x][y] = tile;
	}
	
	public boolean updateMapLightColors(Graphics2D g2, int w, int h) {
		g2.setBackground(BufferTexture.CLEAR_COLOR);
		g2.clearRect(0, 0, w, h);
		for(int x=0; x<=sizex-1; x++)
			for(int y=0; y<=sizey-1; y++) {
				TileActor tile = tiles[x][y];
				if(tile==null || tile.lightPosition==null || tile.lightColor==null)
					continue;
				g2.setColor(new Color(tile.lightColor.x, tile.lightColor.y, tile.lightColor.z));
				g2.fillRect(x+1, y+1, 1, 1);
			}
		return true;
	}
	
	public int getInstanceData(float[] instanceData, PrefabComponent comp) {
		int count = 0;
		int offs = 0;
		for(int x=0; x<=sizex; x++)
			for(int y=0; y<=sizey; y++) {
				TileActor tile = tiles[x][y];
				for(PrefabActor a : tile.prefabs) {
					if(a.prefab.hasComponent(comp)) {
						instanceData[offs] = a.position.x;
						instanceData[offs+1] = a.position.y;
						instanceData[offs+2] = a.position.z;
						instanceData[offs+3] = -a.rotation.y;
						offs += 4;
						count++;
					}
				}
			}
		return count;
	}
	
	public TileActor tileFromPosition(float x, float z) {
		int mapx = (int)(x/6f)+1;
		int mapy = (int)(z/6f)+1;
		return tiles[mapx][mapy];
	}
	
}
