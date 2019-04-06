package com.example.reminder.ui;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.support.v7.widget.helper.ItemTouchHelper;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import android.widget.Toast;

import com.example.reminder.R;
import com.example.reminder.database.GameRoomDatabase;
import com.example.reminder.model.Game;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

public class MainActivity extends AppCompatActivity implements RecyclerView.OnItemTouchListener {

	//instance variables
	private List<Game> mGames;
	private GameAdapter mAdapter;
	private RecyclerView mRecyclerView;

	private GestureDetector mGestureDetector;
	//Constants used when calling the update activity
	public static final String GAME_TOEVOEGEN = "Game toevoegen";
    public static final String GAME_WIJZIGEN = "Game wijzigen";
	public static final int REQUESTCODE_TO_ADD_GAME = 1;
    public static final int REQUESTCODE_TO_EDIT_GAME = 2;
	private int mModifyPosition;

	private GameRoomDatabase db;
	private Executor executor = Executors.newSingleThreadExecutor();

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		db = GameRoomDatabase.getDatabase(this);
		Toolbar toolbar = findViewById(R.id.toolbar);
		setSupportActionBar(toolbar);

		//Initialize the instance variables
		mRecyclerView = findViewById(R.id.recyclerView);
		mGames = new ArrayList<>();

		mRecyclerView.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false));

		mGestureDetector = new GestureDetector(this, new GestureDetector.SimpleOnGestureListener() {
			@Override
			public boolean onSingleTapUp(MotionEvent e) {
				return true;
			}
		});

		FloatingActionButton fab = findViewById(R.id.fab);
		fab.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View view) {

                Intent intent = new Intent(MainActivity.this, AddActivity.class);
                startActivityForResult(intent, REQUESTCODE_TO_ADD_GAME);
			}
		});

		/*
		 * Add a touch helper to the RecyclerView to recognize when a user swipes to delete a list entry.
		 * An ItemTouchHelper enables touch behavior (like swipe and move) on each ViewHolder,
		 * and uses callbacks to signal when a user is performing these actions.
		 */
		ItemTouchHelper.SimpleCallback simpleItemTouchCallback =
				new ItemTouchHelper.SimpleCallback(0, ItemTouchHelper.LEFT | ItemTouchHelper.RIGHT) {
					@Override
					public boolean onMove(RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder, RecyclerView.ViewHolder target) {
						return false;
					}

					//Called when a user swipes left or right on a ViewHolder
					@Override
					public void onSwiped(RecyclerView.ViewHolder viewHolder, int swipeDir) {
						//Get the index corresponding to the selected position
						int position = (viewHolder.getAdapterPosition());
						deleteGame(mGames.get(position));
						mGames.remove(position);
						mAdapter.notifyItemRemoved(position);
					}
				};

		ItemTouchHelper itemTouchHelper = new ItemTouchHelper(simpleItemTouchCallback);
		itemTouchHelper.attachToRecyclerView(mRecyclerView);
		mRecyclerView.addOnItemTouchListener(this);
		getAllGames();

	}

	private void updateUI() {
		if (mAdapter == null) {
			mAdapter = new GameAdapter(mGames);
			mRecyclerView.setAdapter(mAdapter);
		} else {
			mAdapter.swapList(mGames);
		}
	}

	private void getAllGames() {
		executor.execute(new Runnable() {
			@Override
			public void run() {
				mGames = db.gameDao().getAllGames();

				// In a background thread the user interface cannot be updated from this thread.
				// This method will perform statements on the main thread again.
				runOnUiThread(new Runnable() {
					@Override
					public void run() {
						updateUI();
					}
				});
			}
		});
	}

	private void insertGame(final Game game) {
		executor.execute(new Runnable() {
			@Override
			public void run() {
				db.gameDao().insertGame(game);
				getAllGames(); // Because the Room database has been modified we need to get the new list of reminders.
			}
		});
	}

	private void updateGame(final Game game) {
		executor.execute(new Runnable() {
			@Override
			public void run() {
				db.gameDao().updateGame(game);
				getAllGames(); // Because the Room database has been modified we need to get the new list of reminders.
			}
		});
	}

	private void deleteGame(final Game game) {
		executor.execute(new Runnable() {
			@Override
			public void run() {
				db.gameDao().deleteGame(game);
				getAllGames(); // Because the Room database has been modified we need to get the new list of reminders.
			}
		});
	}

    private void deleteAllGames(final List<Game> games) {
        executor.execute(new Runnable() {
            @Override
            public void run() {
                db.gameDao().deleteAllGames(games);
                getAllGames();
            }
        });
    }


	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.menu_main, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == R.id.action_delete_item) {
            if (mGames.isEmpty()) {
                Toast.makeText(MainActivity.this, "Er zijn geen games om te verwijderen", Toast.LENGTH_LONG).show();
            }
            else
            deleteAllGames(mGames);
            return true;
        }

		return super.onOptionsItemSelected(item);
	}

	@Override
	public boolean onInterceptTouchEvent(@NonNull RecyclerView recyclerView, @NonNull MotionEvent motionEvent) {
		View child = recyclerView.findChildViewUnder(motionEvent.getX(), motionEvent.getY());
		if (child != null) {
		    int mAdapterPosition = recyclerView.getChildAdapterPosition(child);
			if (mGestureDetector.onTouchEvent(motionEvent)) {
				Intent intent = new Intent(MainActivity.this, UpdateActivity.class);
				mModifyPosition = mAdapterPosition;
				intent.putExtra(GAME_WIJZIGEN, mGames.get(mAdapterPosition));
				startActivityForResult(intent, REQUESTCODE_TO_EDIT_GAME);
			}
		}
		return false;
	}

	@Override
	public void onTouchEvent(@NonNull RecyclerView recyclerView, @NonNull MotionEvent motionEvent) {

	}

	@Override
	public void onRequestDisallowInterceptTouchEvent(boolean b) {

	}

	@Override
	public void onActivityResult(int requestCode, int resultCode, Intent data) {
		if (requestCode == REQUESTCODE_TO_ADD_GAME) {
			if (resultCode == RESULT_OK) {
				Game nieuweGame = data.getParcelableExtra(MainActivity.GAME_TOEVOEGEN);
				// New timestamp: timestamp of update
				insertGame(nieuweGame);
			}
		}
		else if (requestCode == REQUESTCODE_TO_EDIT_GAME) {
            if (resultCode == RESULT_OK) {
                Game nieuweGame = data.getParcelableExtra(MainActivity.GAME_WIJZIGEN);
                // New timestamp: timestamp of update
                updateGame(nieuweGame);
            }
        }
	}
}
