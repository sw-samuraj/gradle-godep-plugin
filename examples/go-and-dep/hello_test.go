package main

import "testing"

func TestHello(t *testing.T) {
	actual := Hello()
	expected := "Hello, world!"
	if expected != actual {
		t.Errorf("Hello() == %q, want %q", actual, expected)
	}
}
