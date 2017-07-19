--
-- Licensed to the Apache Software Foundation (ASF) under one or more contributor license agreements.
-- http://www.apache.org/licenses/LICENSE-2.0
--

CREATE SCHEMA metadata;
GRANT USAGE ON SCHEMA metadata TO PUBLIC;
COMMENT ON SCHEMA metadata IS 'ISO 19115 metadata';


--
-- CodeLists are represented as enumeration on PostgreSQL.
-- Those declarations will be omitted on databases that do
-- no support enumerations; VARCHAR is used instead.
--
CREATE TYPE "CI_PresentationFormCode" AS ENUM (
  'documentDigital', 'documentHardcopy',
  'imageDigital',    'imageHardcopy',
  'mapDigital',      'mapHardcopy',
  'modelDigital',    'modelHardcopy',
  'profileDigital',  'profileHardcopy',
  'tableDigital',    'tableHardcopy',
  'videoDigital',    'videoHardcopy');

CREATE TYPE "CI_RoleCode" AS ENUM (
  'resourceProvider', 'custodian', 'owner', 'user', 'distributor', 'originator', 'pointOfContact',
  'principalInvestigator', 'processor', 'publisher', 'author', 'sponsor', 'coAuthor', 'collaborator',
  'editor', 'mediator', 'rightsHolder', 'contributor', 'funder', 'stakeholder');

CREATE TYPE "CI_DateTypeCode" AS ENUM (
  'creation', 'publication', 'revision', 'expiry', 'lastUpdate', 'lastRevision', 'nextUpdate',
  'unavailable', 'inForce', 'adopted', 'deprecated', 'superseded', 'validityBegins', 'validityExpires',
  'released', 'distribution');

CREATE CAST (VARCHAR AS metadata."CI_PresentationFormCode") WITH INOUT AS ASSIGNMENT;
CREATE CAST (VARCHAR AS metadata."CI_RoleCode")             WITH INOUT AS ASSIGNMENT;
CREATE CAST (VARCHAR AS metadata."CI_DateTypeCode")         WITH INOUT AS ASSIGNMENT;


--
-- This script creates some tables needed for SIS pre-defined metadata.
-- We do not need to create all tables or all columns in tables here.
-- Missing tables and columns will be added on-the-fly by SIS as needed.
--
-- VARCHAR(15) are for primary keys or foreigner keys.
-- VARCHAR(120) are for character sequences.
--
CREATE TABLE "MD_Identifier" (
  ID          VARCHAR(15) NOT NULL PRIMARY KEY,
  "authority" VARCHAR(15),
  "code"      VARCHAR(120),
  "codeSpace" VARCHAR(120),
  "version"   VARCHAR(120),
  CONSTRAINT fk_identifier_citation FOREIGN KEY ("authority") REFERENCES "CI_Citation" (ID) ON UPDATE RESTRICT ON DELETE RESTRICT);

CREATE TABLE "CI_Party" (
  ID     VARCHAR(15) NOT NULL PRIMARY KEY,
  "name" VARCHAR(120));

CREATE TABLE "CI_Responsibility" (
  ID      VARCHAR(15) NOT NULL PRIMARY KEY,
  "role"  metadata."CI_RoleCode",
  "party" VARCHAR(15) REFERENCES "CI_Party" (ID) ON UPDATE RESTRICT ON DELETE RESTRICT);

CREATE TABLE "CI_Date" (
  ID         VARCHAR(15) NOT NULL PRIMARY KEY,
  "date"     TIMESTAMP,
  "dateType" "CI_DateTypeCode");

CREATE TABLE "CI_Citation" (
  ID                      VARCHAR(15) NOT NULL PRIMARY KEY,
  "title"                 VARCHAR(120),
  "alternateTitle"        VARCHAR(120),
  "date"                  VARCHAR(15) REFERENCES "CI_Date" (ID) ON UPDATE RESTRICT ON DELETE RESTRICT,
  "edition"               VARCHAR(120),
  "editionDate"           TIMESTAMP,
  "identifier"            VARCHAR(15) REFERENCES "MD_Identifier"     (ID) ON UPDATE RESTRICT ON DELETE RESTRICT,
  "citedResponsibleParty" VARCHAR(15) REFERENCES "CI_Responsibility" (ID) ON UPDATE RESTRICT ON DELETE RESTRICT,
  "presentationForm"      "CI_PresentationFormCode");

CREATE TABLE "MD_Format" (
  ID                            VARCHAR(15) NOT NULL PRIMARY KEY,
  "formatSpecificationCitation" VARCHAR(15) REFERENCES "CI_Citation" (ID) ON UPDATE RESTRICT ON DELETE RESTRICT,
  "amendmentNumber"             VARCHAR(120),
  "fileDecompressionTechnique"  VARCHAR(120));


--
-- Metadata about organizations.
--

INSERT INTO "CI_Party" (ID, "name") VALUES
  ('Apache', 'The Apache Software Foundation'),
  ('OGC',    'Open Geospatial Consortium'),
  ('ISO',    'International Organization for Standardization'),
  ('IOGP',   'International Association of Oil & Gas producers'),
  ('NATO',   'North Atlantic Treaty Organization');

INSERT INTO "CI_Responsibility" (ID, "party", "role") VALUES
  ('Apache', 'Apache', 'principalInvestigator'),
  ('OGC',    'OGC',    'principalInvestigator'),
  ('ISO',    'ISO',    'principalInvestigator'),
  ('IOGP',   'IOGP',   'principalInvestigator'),
  ('NATO',   'NATO',   'principalInvestigator');

INSERT INTO "CI_Citation" (ID, "edition", "citedResponsibleParty", "title") VALUES
  ('SIS',         NULL,                  'Apache',  'Apache Spatial Information System'),
  ('ISO 19115-1', 'ISO 19115-1:2014(E)', 'ISO',     'Geographic Information — Metadata Part 1: Fundamentals'),
  ('ISO 19115-2', 'ISO 19115-2:2009(E)', 'ISO',     'Geographic Information — Metadata Part 2: Extensions for imagery and gridded data'),
  ('EPSG',        NULL,                  'IOGP',    'EPSG Geodetic Parameter Dataset'),
  ('MGRS',        NULL,                  'NATO',    'Military Grid Reference System');


--
-- Metadata about file formats.
--
INSERT INTO "CI_Citation" (ID, "alternateTitle", "title") VALUES
  ('GeoTIFF', 'GeoTIFF', 'GeoTIFF Coverage Encoding Profile'),
  ('NetCDF',  'NetCDF',  'NetCDF Classic and 64-bit Offset Format'),
  ('PNG',     'PNG',     'PNG (Portable Network Graphics) Specification'),
  ('CSV',     'CSV',     'Common Format and MIME Type for Comma-Separated Values (CSV) Files'),
  ('CSV-MF',  'CSV',     'OGC Moving Features Encoding Extension: Simple Comma-Separated Values (CSV)'),
  ('GPX',     'GPX',     'GPS Exchange Format');

INSERT INTO "MD_Format" (ID, "formatSpecificationCitation") VALUES
  ('GeoTIFF', 'GeoTIFF'),
  ('NetCDF',  'NetCDF'),
  ('PNG',     'PNG'),
  ('CSV',     'CSV'),
  ('CSV-MF',  'CSV-MF'),
  ('GPX',     'GPX');
