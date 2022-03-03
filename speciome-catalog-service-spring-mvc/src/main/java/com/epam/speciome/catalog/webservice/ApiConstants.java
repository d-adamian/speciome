package com.epam.speciome.catalog.webservice;

public final class ApiConstants {
    public static final String ATTRIBUTES = "/attributes";
    public static final String ARCHIVE = "/archive";
    public static final String UNARCHIVE = "/unarchive";
    public static final String DOWNLOAD = "/download";
    public static final String UPLOAD = "/upload";
    public static final String CSV = "/csv";

    public static final String COLLECTION = "/collection";
    public static final String COLLECTION_ID = "/{collectionId}";
    public static final String COLLECTIONS = "/collections";

    public static final String SAMPLE = "/sample";
    public static final String SAMPLE_ID = "/{sampleId}";
    public static final String SAMPLES = "/samples";

    public static final String USER_DETAILS = "/user-details";
    public static final String NEW_USER = "/new-user";
    public static final String LOGIN = "/login";

    public static final String COLLECTION_BY_ID = COLLECTION + COLLECTION_ID;
    public static final String COLLECTION_BY_ID_ARCHIVE = COLLECTION + COLLECTION_ID + ARCHIVE;
    public static final String COLLECTION_BY_ID_UNARCHIVE = COLLECTION + COLLECTION_ID + UNARCHIVE;

    public static final String SAMPLE_BY_ID = SAMPLE + SAMPLE_ID;
    public static final String SAMPLE_BY_ID_ARCHIVE = SAMPLE + SAMPLE_ID + ARCHIVE;
    public static final String SAMPLE_BY_ID_UNARCHIVE = SAMPLE + SAMPLE_ID + UNARCHIVE;
    public static final String SAMPLES_DOWNLOAD = SAMPLES + DOWNLOAD;
    public static final String SAMPLES_UPLOAD_CSV = SAMPLES + UPLOAD + CSV;
}
