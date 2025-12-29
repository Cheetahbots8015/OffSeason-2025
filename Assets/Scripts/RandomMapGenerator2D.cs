using UnityEngine;
using UnityEngine.Tilemaps;

public class RandomMapGenerator2D : MonoBehaviour
{
    [Header("Tilemaps")]
    [SerializeField] private Tilemap groundTilemap;
    [SerializeField] private Tilemap lightPlatformTilemap;

    [Header("Tiles")]
    [SerializeField] private TileBase groundTile;
    [SerializeField] private TileBase lightPlatformTile;

    [Header("Generation Bounds")]
    [SerializeField] private int minX = -20;
    [SerializeField] private int maxX = 400;
    [SerializeField] private int groundY = -5;

    [Header("Segment Lengths")]
    [SerializeField] private Vector2Int lightPlatformLength = new Vector2Int(6, 8);
    [SerializeField] private Vector2Int gapLength = new Vector2Int(3, 5);
    [SerializeField] private Vector2Int groundLength = new Vector2Int(2, 6);

    [Header("Spawn Prefabs")]
    [SerializeField] private GameObject wallPrefab;
    [SerializeField] private GameObject enemyPrefab;
    [SerializeField] private Transform player;

    [Header("Spawn Settings")]
    [SerializeField, Range(0f, 1f)] private float wallSpawnChance = 0.12f;
    [SerializeField, Range(0f, 1f)] private float enemySpawnChance = 0.08f;
    [SerializeField] private Vector2Int wallHeightRange = new Vector2Int(2, 4);

    private void Start()
    {
        Generate();
    }

    private void Generate()
    {
        if (groundTilemap == null || lightPlatformTilemap == null)
        {
            Debug.LogWarning("Tilemaps are not assigned.");
            return;
        }

        if (player != null)
        {
            player.position = Vector3.zero;
        }

        groundTilemap.ClearAllTiles();
        lightPlatformTilemap.ClearAllTiles();

        int x = minX;
        while (x <= maxX)
        {
            int segmentType = Random.Range(0, 3);
            if (segmentType == 0)
            {
                int length = Random.Range(lightPlatformLength.x, lightPlatformLength.y + 1);
                PlaceLightPlatformSegment(x, length);
                x += length;
            }
            else if (segmentType == 1)
            {
                int length = Random.Range(gapLength.x, gapLength.y + 1);
                x += length;
            }
            else
            {
                int length = Random.Range(groundLength.x, groundLength.y + 1);
                PlaceGroundSegment(x, length);
                x += length;
            }
        }
    }

    private void PlaceLightPlatformSegment(int startX, int length)
    {
        for (int i = 0; i < length; i++)
        {
            Vector3Int cell = new Vector3Int(startX + i, groundY, 0);
            lightPlatformTilemap.SetTile(cell, lightPlatformTile);
        }
    }

    private void PlaceGroundSegment(int startX, int length)
    {
        for (int i = 0; i < length; i++)
        {
            int x = startX + i;
            Vector3Int cell = new Vector3Int(x, groundY, 0);
            groundTilemap.SetTile(cell, groundTile);

            bool spawnedWall = TrySpawnWall(cell);
            if (!spawnedWall)
            {
                TrySpawnEnemy(cell);
            }
        }
    }

    private bool TrySpawnWall(Vector3Int groundCell)
    {
        if (wallPrefab == null || Random.value > wallSpawnChance)
        {
            return false;
        }

        int height = Random.Range(wallHeightRange.x, wallHeightRange.y + 1);
        for (int i = 1; i <= height; i++)
        {
            Vector3Int wallCell = new Vector3Int(groundCell.x, groundCell.y + i, 0);
            Vector3 worldPosition = groundTilemap.GetCellCenterWorld(wallCell);
            Instantiate(wallPrefab, worldPosition, Quaternion.identity, transform);
        }

        return true;
    }

    private void TrySpawnEnemy(Vector3Int groundCell)
    {
        if (enemyPrefab == null || Random.value > enemySpawnChance)
        {
            return;
        }

        Vector3Int enemyCell = new Vector3Int(groundCell.x, groundCell.y + 1, 0);
        Vector3 worldPosition = groundTilemap.GetCellCenterWorld(enemyCell);
        Instantiate(enemyPrefab, worldPosition, Quaternion.identity, transform);
    }
}
