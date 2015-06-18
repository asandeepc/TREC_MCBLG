import org.apache.lucene.document.Field;
import org.apache.lucene.document.FieldType;
import org.apache.lucene.index.IndexOptions;
import org.apache.lucene.util.PagedBytes.Reader;


public class VecTextField extends Field {

    /* Indexed, tokenized, not stored. */
    public static final FieldType TYPE_NOT_STORED = new FieldType();

    /* Indexed, tokenized, stored. */
    public static final FieldType TYPE_STORED = new FieldType();

    static {
    	TYPE_STORED.setIndexOptions(IndexOptions.DOCS_AND_FREQS_AND_POSITIONS);	
    	TYPE_NOT_STORED.setTokenized(true);
    	TYPE_NOT_STORED.setStoreTermVectors(true);
    	TYPE_NOT_STORED.setStoreTermVectorPositions(true);
    	TYPE_NOT_STORED.freeze();

    	TYPE_STORED.setIndexOptions(IndexOptions.DOCS_AND_FREQS_AND_POSITIONS_AND_OFFSETS);
    	TYPE_STORED.setTokenized(true);
    	TYPE_STORED.setStored(true);
    	TYPE_STORED.setStoreTermVectors(true);
    	TYPE_STORED.setStoreTermVectorPositions(true);
    	TYPE_STORED.freeze();
    }

    // TODO: add sugar for term vectors...?

    /** Creates a new TextField with Reader value. */
    public VecTextField(String name, Reader reader, Store store) {
	super(name, store == Store.YES ? TYPE_STORED : TYPE_NOT_STORED);
    }

    /** Creates a new TextField with String value. */
    public VecTextField(String name, String value, Store store) {
	super(name, value, store == Store.YES ? TYPE_STORED : TYPE_NOT_STORED);
    }
}
