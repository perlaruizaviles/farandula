package farandula

import (
	"bytes"
	"encoding/base64"
	"encoding/json"
	"fmt"
	"net/http"
)

func (sabre *SabreGDS) generateCredentials() {
	keydata := []byte(sabreClientKey)
	keystr := base64.StdEncoding.EncodeToString(keydata)

	secretdata := []byte(sabreClientSecret)
	secretstr := base64.StdEncoding.EncodeToString(secretdata)

	credentialsdata := []byte(keystr + ":" + secretstr)
	credentials := base64.StdEncoding.EncodeToString(credentialsdata)
	sabre.credentials = credentials
}

func (sabre *SabreGDS) generateToken() error {
	var token sabreToken
	data := []byte(`grant_type=client_credentials`)
	req, err := http.NewRequest("POST", sabreTokenUrl, bytes.NewBuffer(data))
	if err != nil {
		return err
	}
	req.Header.Set("Content-Type", "application/x-www-form-urlencoded")
	req.Header.Set("Authorization", "Basic "+sabre.credentials)
	resp, err := sabre.client.Do(req)
	if err != nil {
		return err
	}
	if resp.Status != "200 OK" {
		return fmt.Errorf("generating token: reponse status = %v", resp.Status)
	}
	err = json.NewDecoder(resp.Body).Decode(&token)
	if err != nil {
		return err
	}
	sabre.token = token
	return nil
}
