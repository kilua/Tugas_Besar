package addres.book;


import android.app.Activity;
import android.database.Cursor;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

public class EditAddress extends Activity
{

	private EditText mTitleText;
    private EditText mBodyText;
    private Long mRowId;
    private DBAddress mDbHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        mDbHelper = new DBAddress(this);
        mDbHelper.open();
        setContentView(R.layout.address_edit);
        
       
        mTitleText = (EditText) findViewById(R.id.nama);
        mBodyText = (EditText) findViewById(R.id.hp);
      
        Button confirmButton = (Button) findViewById(R.id.confirm);
       
        mRowId = savedInstanceState != null ? savedInstanceState.getLong(DBAddress.KEY_ROWID) 
                							: null;
		if (mRowId == null)
		{
			Bundle extras = getIntent().getExtras();            
			mRowId = extras != null ? extras.getLong(DBAddress.KEY_ROWID) 
									: null;
		}

		populateFields();
		
        confirmButton.setOnClickListener(new View.OnClickListener()
        {

        	public void onClick(View view)
        	{
        	    setResult(RESULT_OK);
        	    finish();
        	}
          
        });
    }
    
    private void populateFields()
    {
        if (mRowId != null)
        {
            Cursor address = mDbHelper.fetchAddress(mRowId);
            startManagingCursor(address);
            mTitleText.setText(address.getString(address.getColumnIndexOrThrow(DBAddress.KEY_NAMA)));
            mBodyText.setText(address.getString(address.getColumnIndexOrThrow(DBAddress.KEY_HP)));
        }
    }
    
    @Override
    protected void onSaveInstanceState(Bundle outState)
    {
        super.onSaveInstanceState(outState);
        outState.putLong(DBAddress.KEY_ROWID, mRowId);
    }
    
    @Override
    protected void onPause()
    {
        super.onPause();
        saveState();
    }
    
    @Override
    protected void onResume()
    {
        super.onResume();
        populateFields();
    }
    
    private void saveState()
    {
        String nama = mTitleText.getText().toString();
        String hp = mBodyText.getText().toString();

        if (mRowId == null)
        {
            long id = mDbHelper.createAddress(nama, hp);
            if (id > 0)
            {
                mRowId = id;
            }
        }
        else
        {
            mDbHelper.updateAddress(mRowId, nama, hp);
        }
    }
    
}
